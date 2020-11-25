package cn.itbat.microsoft.service.impl;


import cn.itbat.microsoft.cache.GraphCache;
import cn.itbat.microsoft.config.GraphProperties;
import cn.itbat.microsoft.enums.AccountStatusEnum;
import cn.itbat.microsoft.enums.CapabilityStatusEnum;
import cn.itbat.microsoft.enums.RefreshTypeEnum;
import cn.itbat.microsoft.model.GraphUser;
import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.service.GraphService;
import cn.itbat.microsoft.service.MailService;
import cn.itbat.microsoft.service.Microsoft365Service;
import cn.itbat.microsoft.utils.HttpClientUtils;
import cn.itbat.microsoft.utils.PageInfo;
import cn.itbat.microsoft.utils.PasswordGenerator;
import cn.itbat.microsoft.vo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.AssignedLicense;
import com.microsoft.graph.models.extensions.Domain;
import com.microsoft.graph.models.extensions.SubscribedSku;
import com.microsoft.graph.models.extensions.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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

    private static final String CHINA = "china";

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
            return null;
        }
        List<SubscribedSkuVo> subscribedSkuVos = new ArrayList<>();
        for (SubscribedSku subscribedSku : subscribedSkus) {
            subscribedSkuVos.add(this.getSubscribedSkuVo(appName, subscribedSku));
        }
        return subscribedSkuVos;
    }

    private SubscribedSkuVo getSubscribedSkuVo(String appName, SubscribedSku subscribedSku) {
        JsonObject subscribedSkuRawObject = subscribedSku.getRawObject();
        Integer enabled = subscribedSku.prepaidUnits.enabled;
        Integer suspended = subscribedSku.prepaidUnits.suspended;
        Integer warning = subscribedSku.prepaidUnits.warning;
        Gson gson = new Gson();
        SubscribedSkuVo subscribedSkuVo = gson.fromJson(subscribedSkuRawObject, SubscribedSkuVo.class);
        subscribedSkuVo.setEnabled(enabled);
        subscribedSkuVo.setSuspended(suspended);
        subscribedSkuVo.setWarning(warning);
        subscribedSkuVo.setDisplayStatus(CapabilityStatusEnum.getName(subscribedSkuVo.getCapabilityStatus()));
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
    public PageInfo<GraphUserVo> getGraphUserVos(GraphUserVo graphUserVo, Pager pager) {
        // 判断是否有条件
        List<User> users;
        if (StringUtils.isEmpty(graphUserVo.getDisplayName()) && StringUtils.isEmpty(graphUserVo.getUserPrincipalName())) {
            users = graphCache.getUsersCache(graphUserVo.getAppName());
        } else {
            users = graphService.getUsers(graphUserVo);
        }
        users = users.stream().filter(l -> !l.userPrincipalName.contains("admin")).collect(Collectors.toList());
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
        List<GraphUserVo> graphUserVos = users.stream().map(this::getGraphUserVo).collect(Collectors.toList());
        // 过滤出许可证
        if (!StringUtils.isEmpty(graphUserVo.getSkuId())) {
            graphUserVos.removeIf(vo -> CollectionUtils.isEmpty(vo.getSkuVos()) || !vo.getSkuVos().stream().map(SkuVo::getSkuId).collect(Collectors.toList()).contains(graphUserVo.getSkuId()));
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
        JsonElement createdDateTime = user.getRawObject().get("createdDateTime");
        graphUserVo.setCreatedDateTime(createdDateTime == null ? "" : createdDateTime.getAsString());
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
        // 构建用户对象
        GraphUser graphUser = GraphUser.builder()
                .country("中国")
                .displayName(graphUserVo.getDisplayName())
                .mailNickname(graphUserVo.getMailNickname())
                .userPrincipalName(graphUserVo.getMailNickname() + "@" + graphUserVo.getDomain())
                .password(graphUserVo.getPassword() == null ? "" : graphUserVo.getPassword())
                .skuId(StringUtils.isEmpty(graphUserVo.getSkuId()) ? graphProperties.getSubConfig(graphUserVo.getSkuType()).getSkuId() : graphUserVo.getSkuId())
                .build();
        log.info("【Office】创建用户开始：" + JSONObject.toJSONString(graphUser));
        // 调用api创建用户
        User user = graphService.createUser(graphUserVo.getAppName(), graphUser);
        log.info("【Office】创建用户成功：" + user.getRawObject().toString());
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
    }

    @Async("asyncPoolTaskExecutor")
    @Override
    public void createBatch(Integer num, String appName, String region, String skuId, String domain) {
        //
        String s = HttpClientUtils.sendGet("http://api.neton.ml/api", "amount=" + num + "&region=" + region + "&ext");
        List<UserVo> userVoList = JSON.parseArray(s, UserVo.class);
        if (CollectionUtils.isEmpty(userVoList)) {
            return;
        }
        if (StringUtils.isEmpty(domain)) {
            domain = graphCache.getDomainCache(appName).stream().filter(l -> l.isDefault).collect(Collectors.toList()).get(0).id;
        }
        List<UserVo> unique = userVoList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(
                        () -> new TreeSet<>((o1, o2) -> {
                            if (o1.getName().compareTo(o2.getName()) == 0) {
                                return 0;
                            } else {
                                return o1.getName().compareTo(o2.getName());
                            }
                        }))
                , ArrayList::new)
        );

        for (UserVo userVo : unique) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String mailNickname = userVo.getName().toLowerCase();
            if (CHINA.equals(region)) {
                mailNickname = Pinyin.toPinyin(userVo.getName() + userVo.getSurname(), "").toLowerCase();
            }
            GraphUser graphUser = GraphUser.builder()
                    .country("中国")
                    .surname(userVo.getSurname())
                    .givenName(userVo.getName())
                    .displayName(CHINA.equals(region) ? userVo.getName() + userVo.getSurname() : userVo.getSurname() + " " + userVo.getName())
                    .mailNickname(mailNickname)
                    .mobilePhone(userVo.getPhone())
                    .userPrincipalName(mailNickname + "@" + domain)
                    .password("P" + new PasswordGenerator(6, 3).generateRandomPassword() + "&")
                    .skuId(skuId).build();
            log.info("【Office】创建用户开始：" + JSONObject.toJSONString(graphUser));
            try {
                User user = graphService.createUser(appName, graphUser);
                log.info("【Office】创建用户成功：" + user.getRawObject().toString());
            } catch (Exception e) {
                log.error("【Office】创建用户失败：" + e.getMessage());
                // 将创建的用户删除，如果是订阅分配失败的话
            }
        }
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
        homePageVo.setNoLandingUsers(this.getGraphUserVos(graphUserVo, new Pager(1, 10)).getList());
        graphUserVo.setAccountEnabled(null);
        graphUserVo.setAssignLicense(false);
        homePageVo.setUnauthorizedUsers(this.getGraphUserVos(graphUserVo, new Pager(1, 10)).getList());
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


}



