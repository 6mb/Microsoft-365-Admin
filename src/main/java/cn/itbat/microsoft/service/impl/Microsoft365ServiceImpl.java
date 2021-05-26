package cn.itbat.microsoft.service.impl;


import cn.binarywang.tools.generator.ChineseAddressGenerator;
import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.binarywang.tools.generator.ChineseNameGenerator;
import cn.itbat.microsoft.cache.GraphCache;
import cn.itbat.microsoft.config.GraphProperties;
import cn.itbat.microsoft.enums.AccountStatusEnum;
import cn.itbat.microsoft.enums.RefreshTypeEnum;
import cn.itbat.microsoft.model.GraphUser;
import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.service.GraphService;
import cn.itbat.microsoft.service.MailService;
import cn.itbat.microsoft.service.Microsoft365Service;
import cn.itbat.microsoft.utils.PageInfo;
import cn.itbat.microsoft.utils.PasswordGenerator;
import cn.itbat.microsoft.vo.*;
import com.alibaba.fastjson.JSONObject;
import com.github.promeg.pinyinhelper.Pinyin;
import com.microsoft.graph.models.AssignedLicense;
import com.microsoft.graph.models.Domain;
import com.microsoft.graph.models.SubscribedSku;
import com.microsoft.graph.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Microsoft 365
 *
 * @author mjj
 * @date 2020年05月13日 10:17:33
 */
@Slf4j
@Service("microsoft365Service")
public class Microsoft365ServiceImpl implements Microsoft365Service {

    @Resource
    private GraphService graphService;

    @Resource
    private MailService mailService;

    @Resource
    private GraphProperties graphProperties;

    @Resource
    private GraphCache graphCache;


    @Override
    public void refresh(String appName, Integer type) {
        if (type == null) {
            graphCache.refreshUsers(appName);
            graphCache.refreshLicense(appName);
            graphCache.refreshDomain(appName);
            graphCache.refreshRole(appName);
            log.info("【Microsoft 365】全部刷新成功");
            return;
        }
        if (RefreshTypeEnum.USER.getType().equals(type)) {
            graphCache.refreshUsers(appName);
        }
        if (RefreshTypeEnum.SUB.getType().equals(type)) {
            graphCache.refreshLicense(appName);
        }
        if (RefreshTypeEnum.DOMAIN.getType().equals(type)) {
            graphCache.refreshDomain(appName);
        }
        log.info("【Microsoft 365】{} 刷新成功", RefreshTypeEnum.getName(type));
    }

    @Override
    public List<SubscribedSkuVo> getSubscribed(String appName) {
        List<SubscribedSku> subscribedSkus = graphCache.getLicenseCache(appName);
        if (CollectionUtils.isEmpty(subscribedSkus)) {
            return new ArrayList<>();
        }
        List<SubscribedSkuVo> subscribedSkuVos = new ArrayList<>();
        for (SubscribedSku subscribedSku : subscribedSkus) {
            subscribedSkuVos.add(this.getSubscribedSkuVo(appName, subscribedSku));
        }
        return subscribedSkuVos;
    }

    private SubscribedSkuVo getSubscribedSkuVo(String appName, SubscribedSku subscribedSku) {
        SubscribedSkuVo subscribedSkuVo = new SubscribedSkuVo(subscribedSku);
        String displayName = graphProperties.getSubConfigDisplayName(subscribedSkuVo.getSkuId());
        subscribedSkuVo.setSkuName(StringUtils.isEmpty(displayName) ? subscribedSkuVo.getSkuPartNumber() : displayName);
        subscribedSkuVo.setAppName(appName);
        return subscribedSkuVo;
    }

    @Override
    public List<DomainVo> getDomainVo(String appName) {
        List<Domain> domains = graphCache.getDomainCache(appName);
        if (CollectionUtils.isEmpty(domains)) {
            return null;
        }
        List<DomainVo> domainVos = new ArrayList<>();
        for (Domain domain : domains) {
            if (!domain.isVerified) {
                continue;
            }
            DomainVo domainVo = new DomainVo();
            domainVo.setId(domain.id);
            domainVo.setIsDefault(domain.isDefault);
            domainVo.setDisplayIsDefault(domain.isDefault ? "是" : "否");
            domainVo.setIsRoot(domain.isRoot);
            domainVo.setDisplayIsRoot(domain.isRoot ? "是" : "否");
            domainVos.add(domainVo);
        }
        return domainVos;
    }

    @Override
    public GraphUserVo getGraphUserVo(String appName, String userId) {
        return this.getGraphUserVo(graphService.getUser(appName, userId));
    }

    @Override
    public PageInfo<GraphUserVo> getGraphUserVos(String appName, Pager pager) {
        List<User> users = graphCache.getUsersCache(appName);
        if (CollectionUtils.isEmpty(users)) {
            return new PageInfo<>();
        }
        return new PageInfo<>(users.stream().map(this::getGraphUserVo).collect(Collectors.toList()), pager);
    }

    @Override
    public PageInfo<GraphUserVo> getGraphUserVos(GraphUserVo graphUserVo, Pager pager, GraphUserSorterVo sorter) {
        // 判断是否有条件
        List<User> users;
        if (StringUtils.isEmpty(graphUserVo.getDisplayName()) && StringUtils.isEmpty(graphUserVo.getUserPrincipalName())) {
            users = graphCache.getUsersCache(graphUserVo.getAppName());
        } else {
            users = graphService.getUsers(graphUserVo);
        }
//        users = users.stream().filter(l -> !l.userPrincipalName.contains("admin")).collect(Collectors.toList());
        // 判断是启用禁用的情况
        if (graphUserVo.getAccountEnabled() != null) {
            users = users.stream().filter(l -> l.accountEnabled.equals(graphUserVo.getAccountEnabled())).collect(Collectors.toList());
        }
        // 判断是否授权的情况
        if (graphUserVo.getAssignLicense() != null && StringUtils.isEmpty(graphUserVo.getDisplayName()) && StringUtils.isEmpty(graphUserVo.getUserPrincipalName())) {
            users = users.stream().filter(l -> graphUserVo.getAssignLicense() != CollectionUtils.isEmpty(l.assignedLicenses)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(users)) {
            return new PageInfo<>();
        }

        // 根据创建时间排序
        if (StringUtils.hasLength(sorter.getSortcreatedDateTime())) {
            if (sorter.getSortcreatedDateTime().equals("ASC")) {
                users.sort(Comparator.comparing(u -> u.createdDateTime));
            } else {
                users.sort(Comparator.comparing((User u) -> u.createdDateTime).reversed());
            }
        }

        // 根据用户名排序
        if (StringUtils.hasLength(sorter.getSortuserPrincipalName())) {
            if (sorter.getSortuserPrincipalName().equals("ASC")) {
                users.sort(Comparator.comparing(u -> u.userPrincipalName));
            } else {
                users.sort(Comparator.comparing((User u) -> u.userPrincipalName).reversed());
            }
        }

        //根据地区排序
        if (StringUtils.hasLength(sorter.getSortusageLocation())) {
            if (sorter.getSortusageLocation().equals("ASC")) {
                users.sort(Comparator.comparing(u -> u.usageLocation));
            } else {
                users.sort(Comparator.comparing((User u) -> u.usageLocation).reversed());
            }
        }


        List<GraphUserVo> graphUserVos = users.stream().map(this::getGraphUserVo).collect(Collectors.toList());
        // 过滤出许可证
        if (!StringUtils.isEmpty(graphUserVo.getSkuId())) {
            graphUserVos.removeIf(vo -> CollectionUtils.isEmpty(vo.getSkuVos()) || !vo.getSkuVos().stream().map(SkuVo::getSkuId).collect(Collectors.toList()).contains(graphUserVo.getSkuId()));
        }


        // 给用户添加角色属性
        Map<DirectoryRoleVo, Set<String>> map = graphCache.getRoleCache(graphUserVo.getAppName());
        map.keySet().forEach(r -> graphUserVos.forEach(u -> {
            if (map.get(r).contains(u.getUserPrincipalName())) {
                u.getDirectoryRoles().add(r);
            }
        }));

        // 根据角色数量排序
        if (StringUtils.hasLength(sorter.getSortroles())) {
            if (sorter.getSortroles().equals("ASC")) {
                graphUserVos.sort(Comparator.comparing(u -> u.getDirectoryRoles().size()));
            } else {
                graphUserVos.sort(Comparator.comparing((GraphUserVo u) -> u.getDirectoryRoles().size()).reversed());
            }
        }

        return new PageInfo<>(graphUserVos, pager);
    }

    private GraphUserVo getGraphUserVo(User user) {
        // 过滤掉admin 用户
        GraphUserVo graphUserVo = new GraphUserVo();
        graphUserVo.setUserId(user.id);
        graphUserVo.setSurname(user.surname);
        graphUserVo.setGivenName(user.givenName);
        graphUserVo.setMailNickname(user.mailNickname);
        graphUserVo.setDisplayName(user.displayName);
        graphUserVo.setUserPrincipalName(user.userPrincipalName);
        graphUserVo.setUsageLocation(user.usageLocation);
        graphUserVo.setCountry(user.country);
        graphUserVo.setCity(user.city);
        graphUserVo.setState(user.state);
        graphUserVo.setStreetAddress(user.streetAddress);
        graphUserVo.setAccountEnabled(user.accountEnabled);
        graphUserVo.setDisplayAccountEnable(AccountStatusEnum.getName(graphUserVo.getAccountEnabled()));
        if (user.createdDateTime == null) {
            graphUserVo.setCreatedDateTime("");
        } else {
            graphUserVo.setCreatedDateTime(user.createdDateTime.toString());
        }
        List<AssignedLicense> assignedLicenses = user.assignedLicenses;
        if (!CollectionUtils.isEmpty(assignedLicenses)) {
            List<SkuVo> skuVos = new ArrayList<>();
            for (AssignedLicense assignedLicense : assignedLicenses) {
                String skuName = graphProperties.getSubConfigDisplayName(assignedLicense.skuId.toString());
                if (StringUtils.isEmpty(skuName)) {
                    skuName = "存在订阅";
                }
                skuVos.add(SkuVo.builder()
                        .skuId(assignedLicense.skuId.toString())
                        .skuType(graphProperties.getSubConfigName(assignedLicense.skuId.toString()))
                        .skuName(skuName).build());
            }
            graphUserVo.setSkuVos(skuVos);
        }
        return graphUserVo;
    }

    @Override
    public GraphUserVo create(GraphUserVo graphUserVo) {
        if (StringUtils.isEmpty(graphUserVo.getDomain())) {
            graphUserVo.setDomain(graphCache.getDomainCache(graphUserVo.getAppName()).stream().filter(l -> l.isDefault).collect(Collectors.toList()).get(0).id);
        }
        if (StringUtils.isEmpty(graphUserVo.getUsageLocation())) {
            graphUserVo.setUsageLocation(graphProperties.listUsageLocation(graphUserVo.getAppName())[0]);
        }
        // 构建用户对象
        GraphUser graphUser = GraphUser.builder()
                .usageLocation(graphUserVo.getUsageLocation())
                .displayName(graphUserVo.getDisplayName())
                .mailNickname(graphUserVo.getMailNickname())
                .userPrincipalName(graphUserVo.getMailNickname() + "@" + graphUserVo.getDomain())
                .password(graphUserVo.getPassword() == null ? "" : graphUserVo.getPassword())
                .skuId(StringUtils.isEmpty(graphUserVo.getSkuId()) ? graphProperties.getSubConfig(graphUserVo.getSkuType()).getSkuId() : graphUserVo.getSkuId())
                .build();
        log.info("【Office】创建用户开始：" + JSONObject.toJSONString(graphUser));
        // 调用api创建用户
        User user = graphService.createUser(graphUserVo.getAppName(), graphUser);
        log.info("【Office】创建用户成功：" + user.toString());
        GraphUserVo graphUserVoResult = this.getGraphUserVo(user);
        graphUserVoResult.setPassword(graphUser.getPassword());
        // 发送邮件
        if (!StringUtils.isEmpty(graphUserVo.getMailbox())) {
            mailService.sendMail(graphUserVo.getMailbox(), graphUser.getUserPrincipalName(), graphUserVoResult.getPassword());
        }
        // 清空原来的缓存
        return graphUserVoResult;
    }

    @Override
    public void updateUser(String appName, GraphUser graphUser) {
        log.info("【Office】组织：" + appName + " 更新用户" + JSONObject.toJSONString(graphUser));
        graphService.updateUser(appName, graphUser);
    }

    @Override
    public void enableDisableUser(String appName, String userId, Boolean accountEnabled) {
        graphService.enableDisableUser(appName, userId, accountEnabled);
        log.info("【Office】组织：" + appName + " 用户账户" + (accountEnabled ? "启用" : "禁用") + "成功");
    }

    @Override
    public GraphUserVo addLicense(String appName, String userId, String skuId) {
        User user = graphService.addLicense(appName, userId, skuId);
        log.info("【Office】组织：" + appName + " 添加" + userId + "订阅" + skuId + "成功");
        return this.getGraphUserVo(user);
    }

    @Override
    public GraphUserVo cancelLicense(String appName, String userId, String skuId) {
        if (StringUtils.isEmpty(skuId)) {
            // 取消所有订阅
            User user = graphService.getUser(appName, userId);
            List<AssignedLicense> assignedLicenses = user.assignedLicenses;
            if (!CollectionUtils.isEmpty(assignedLicenses)) {
                for (AssignedLicense assignedLicens : assignedLicenses) {
                    graphService.cancelLicense(appName, assignedLicens.skuId.toString(), userId);
                }
            }
            return null;
        }
        User user = graphService.cancelLicense(appName, userId, skuId);
        log.info("【Office】组织：" + appName + " 取消" + userId + "订阅" + skuId + "成功");
        return this.getGraphUserVo(user);
    }

    @Override
    public void resetPassword(String appName, String userName, String password) {
        graphService.getUser(appName, userName);
        String pw = StringUtils.isEmpty(password) ? "P" + new PasswordGenerator(6, 3).generateRandomPassword() + "&" : password;
        graphService.resetPassword(appName, userName, StringUtils.isEmpty(password) ? pw : password);
        log.info("【Office】用户重置密码成功：{}", StringUtils.isEmpty(password) ? pw : password);
    }

    @Override
    public void deletedUser(String appName, String userName) {
        graphService.deletedUser(appName, userName);
        log.info("【Office】组织：" + appName + " 删除用户成功：" + userName);
    }

    @Async("asyncPoolTaskExecutor")
    @Override
    public void deletedUsers(String appName) {
        List<User> users = graphService.getUsers(appName);
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        for (User user : users) {
            if (user.userPrincipalName.contains("admin")) {
                continue;
            }
            try {
                Thread.sleep(50);
                graphService.deletedUser(appName, user.id);
            } catch (Exception e) {
                log.error("【Office】删除用户失败：" + user.userPrincipalName);
            }
        }
        graphCache.refreshUsers(appName);
    }


    @Async("asyncPoolTaskExecutor")
    @Override
    public void createBatch(Integer num, String appName, String skuId, String domain, String password, String usageLocation) {
        if (StringUtils.isEmpty(domain)) {
            domain = graphCache.getDomainCache(appName).stream().filter(l -> l.isDefault).collect(Collectors.toList()).get(0).id;
        }
        if (StringUtils.isEmpty(usageLocation)) {
            usageLocation = graphProperties.listUsageLocation(appName)[0];
        }
        for (int i = 0; i < num; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String displayName = ChineseNameGenerator.getInstance().generate();
            String mailNickname = Pinyin.toPinyin(displayName, "").toLowerCase();
            GraphUser graphUser = GraphUser.builder()
                    .country("中国")
                    .city("LOC")
                    .usageLocation(usageLocation)
                    .displayName(displayName)
                    .mailNickname(mailNickname)
                    .mobilePhone(ChineseMobileNumberGenerator.getInstance().generate())
                    .streetAddress(ChineseAddressGenerator.getInstance().generate())
                    .userPrincipalName(mailNickname + "@" + domain)
                    .password(StringUtils.isEmpty(password) ? "P" + new PasswordGenerator(6, 3).generateRandomPassword() + "&" : password)
                    .skuId(skuId).build();
            log.info("【Office】创建用户开始：" + JSONObject.toJSONString(graphUser));
            try {
                User user = graphService.createUser(appName, graphUser);
                log.info("【Office】创建用户成功：" + user.toString());
            } catch (Exception e) {
                log.error("【Office】创建用户失败：" + e.getMessage());
                // 将创建的用户删除，如果是订阅分配失败的话
            }
        }
        graphCache.refreshUsers(appName);
    }


    @Override
    public HomePageVo homePage(String appName) {
        HomePageVo homePageVo = new HomePageVo();
        StatisticsVo statisticsVo = new StatisticsVo();
        this.getLicensesStatisticsVo(appName, statisticsVo);
        this.getUsersStatisticsVo(appName, statisticsVo);
        homePageVo.setStatisticsVo(statisticsVo);
        GraphUserVo graphUserVo = new GraphUserVo();
        graphUserVo.setAppName(appName);
        graphUserVo.setTop(10);
        graphUserVo.setAccountEnabled(false);
        homePageVo.setNoLandingUsers(this.getGraphUserVos(graphUserVo, new Pager(1, 10), new GraphUserSorterVo()).getList());
        graphUserVo.setAccountEnabled(null);
        graphUserVo.setAssignLicense(false);
        homePageVo.setUnauthorizedUsers(this.getGraphUserVos(graphUserVo, new Pager(1, 10), new GraphUserSorterVo()).getList());
        return homePageVo;
    }


    private void getLicensesStatisticsVo(String appName, StatisticsVo statisticsVo) {
        List<SubscribedSkuVo> subscribedSkuVos = this.getSubscribed(appName);
        if (!CollectionUtils.isEmpty(subscribedSkuVos)) {
            statisticsVo.setProductSubs(subscribedSkuVos.size());
            statisticsVo.setLicenses(subscribedSkuVos.stream().map(SubscribedSkuVo::getEnabled).reduce(Integer::sum).orElse(null));
            statisticsVo.setAllocatedLicenses(subscribedSkuVos.stream().map(SubscribedSkuVo::getConsumedUnits).reduce(Integer::sum).orElse(null));
            statisticsVo.setAvailableLicenses(statisticsVo.getLicenses() - statisticsVo.getAllocatedLicenses());
        }
    }

    private void getUsersStatisticsVo(String appName, StatisticsVo statisticsVo) {
        List<User> users = graphCache.getUsersCache(appName);
        if (!CollectionUtils.isEmpty(users)) {
            statisticsVo.setUsers(users.size());
            statisticsVo.setAllowedUsers((int) users.stream().filter(l -> l.accountEnabled).count());
            statisticsVo.setBiddenUsers((int) users.stream().filter(l -> !l.accountEnabled).count());
            statisticsVo.setUnauthorizedUsers((int) users.stream().filter(l -> CollectionUtils.isEmpty(l.assignedLicenses)).count());
        }
    }

    @Override
    public StatisticsVo getLicenseStatistics(String appName) {
        StatisticsVo statisticsVo = new StatisticsVo();
        this.getLicensesStatisticsVo(appName, statisticsVo);
        return statisticsVo;
    }

    @Override
    public StatisticsVo getUsersStatistics(String appName) {
        StatisticsVo statisticsVo = new StatisticsVo();
        this.getUsersStatisticsVo(appName, statisticsVo);
        return statisticsVo;
    }

    @Override
    public List<DirectoryRoleVo> listRoles(String appName) {
        return new ArrayList<>(graphCache.getRoleCache(appName).keySet());
    }

    @Override
    public Boolean addDirectoryRoleMember(String appName, String userId, String roleId) {
        return graphService.addDirectoryRoleMember(appName, userId, roleId);
    }


}



