package cn.itbat.microsoft.controller;

import cn.itbat.microsoft.config.GraphProperties;
import cn.itbat.microsoft.model.GraphUser;
import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.service.Microsoft365Service;
import cn.itbat.microsoft.vo.BaseResultVo;
import cn.itbat.microsoft.vo.GraphUserVo;
import cn.itbat.microsoft.vo.SubscribedSkuVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Microsoft 365
 *
 * @author mjj
 * @date 2020年08月11日 09:50:37
 */
@Slf4j
@RestController
@RequestMapping("/microsoft/365")
public class Microsoft365Controller {
    private static final Integer NUM = 200;

    @Resource
    private Microsoft365Service microsoft365Service;

    @Resource
    private GraphProperties graphProperties;

    /**
     * 查询组织类型
     *
     * @return 组织类型
     */
    @GetMapping("/getAppName")
    public BaseResultVo getAppName() {
        return BaseResultVo.success(graphProperties.getConfigs().stream().map(GraphProperties.GraphConfig::getAppName));
    }

    /**
     * 查询绑定域名
     *
     * @param appName 组织类型
     * @return 域名
     */
    @GetMapping("/getDomains")
    public BaseResultVo getDomains(String appName) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getDomainVo(appName));
    }

    /**
     * 首页展示
     *
     * @param appName 组织类型
     * @return 首页数据
     */
    @GetMapping("/homePage")
    public BaseResultVo homePage(String appName) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.homePage(appName));
    }

    /**
     * 许可统计
     *
     * @param appName 组织类型
     * @return 许可统计
     */
    @GetMapping("/getLicenseStatistics")
    public BaseResultVo getLicenseStatistics(String appName) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getLicenseStatistics(appName));
    }

    /**
     * 许可证列表查询
     *
     * @param appName 组织类型
     * @param skuId   许可
     * @return 许可信息
     */
    @GetMapping("/listLicense")
    public BaseResultVo listLicense(String appName, String skuId) {

        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        List<SubscribedSkuVo> subscribed = microsoft365Service.getSubscribed(appName);
        if (StringUtils.isNotBlank(skuId)) {
            subscribed = subscribed.stream().filter(l -> l.getSkuId().equals(skuId)).collect(Collectors.toList());
        }
        return BaseResultVo.success(subscribed);
    }

    /**
     * 用户统计
     *
     * @param appName 组织类型
     * @return 用户统计信息
     */
    @GetMapping("/getUsersStatistics")
    public BaseResultVo getUsersStatistics(String appName) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getUsersStatistics(appName));
    }

    /**
     * 查询用户信息:列表
     *
     * @param graphUserVo 查询参数
     * @param pager       分页参数
     * @return 用户信息
     */
    @GetMapping("/listUsers")
    public BaseResultVo listUsers(GraphUserVo graphUserVo, Pager pager) {

        if (StringUtils.isBlank(graphUserVo.getAppName())) {
            return BaseResultVo.error("参数为空！");
        }
        if (graphProperties.getConfig(graphUserVo.getAppName()) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getGraphUserVos(graphUserVo, pager));
    }

    /**
     * 查询用户信息：详情
     *
     * @param appName 组织名称
     * @param userId  用户邮箱或userId
     * @return 用户信息
     */
    @GetMapping("/getUser")
    public BaseResultVo getUser(String appName, String userId) {
        if (StringUtils.isBlank(appName) || StringUtils.isBlank(userId)) {
            return BaseResultVo.error("参数为空！");
        }
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getGraphUserVo(appName, userId));
    }

    /**
     * 批量查询用户信息
     *
     * @param appName 组织名称
     * @param pager   分页参数
     * @return 用户信息
     */
    @GetMapping("/getUsersAll")
    public BaseResultVo getUsersAll(String appName, Pager pager) {
        if (StringUtils.isBlank(appName)) {
            return BaseResultVo.error("参数为空！");
        }
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.getGraphUserVos(appName, pager));
    }

    /**
     * 添加账号
     *
     * @param graphUserVo 账号信息
     * @return 成功失败
     */
    @PostMapping("/addUser")
    public BaseResultVo addUser(GraphUserVo graphUserVo) {
        if (graphProperties.getConfig(graphUserVo.getAppName()) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        GraphUser graphUser = microsoft365Service.create(graphUserVo);
        return BaseResultVo.success(graphUser);
    }

    /**
     * 删除用户信息
     *
     * @param appName 组织名称
     * @param userId  用户邮箱或userId
     * @return 成功失败
     */
    @PostMapping("/deletedUser")
    public BaseResultVo deletedUser(String appName, String userId) {
        if (StringUtils.isBlank(appName) || StringUtils.isBlank(userId)) {
            return BaseResultVo.error("参数为空！");
        }
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        microsoft365Service.deletedUser(appName, userId);
        return BaseResultVo.success();
    }

    /**
     * 添加订阅
     *
     * @param appName 组织名称
     * @param userId  用户id
     * @param skuId   许可Id
     * @return 成功失败
     */
    @PostMapping("/addLicense")
    public BaseResultVo addLicense(String appName, String userId, String skuId) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.addLicense(appName, userId.trim(), skuId));
    }

    /**
     * 取消订阅
     *
     * @param appName 组织名称
     * @param userId  用户id
     * @param skuId   许可Id
     * @return 成功失败
     */
    @PostMapping("/cancelLicense")
    public BaseResultVo cancelLicense(String appName, String userId, String skuId) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        return BaseResultVo.success(microsoft365Service.cancelLicense(appName, userId.trim(), skuId));
    }

    /**
     * 启用、禁用账户
     *
     * @param appName        组织名称
     * @param userId         用户邮箱或userId
     * @param accountEnabled 启用/禁用
     * @return 成功失败
     */
    @PostMapping("/enableDisableUser")
    public BaseResultVo enableDisableUser(String appName, String userId, Boolean accountEnabled) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        microsoft365Service.enableDisableUser(appName, userId.trim(), accountEnabled);
        return BaseResultVo.success();
    }

    /**
     * 刷新缓存
     *
     * @param appName 组织名称
     * @param type    刷新类型【"1：用户；2：订阅；3：域名"】
     * @return
     */
    @GetMapping("/refresh")
    public BaseResultVo refresh(String appName, Integer type) {
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        microsoft365Service.refresh(appName, type);
        return BaseResultVo.success();
    }

    @PostMapping("/deletedUserBatch")
    public BaseResultVo deletedUserBatch(String appName) {
        microsoft365Service.deletedUsers(appName);
        return BaseResultVo.success();
    }

    /**
     * 批量创建用户信息
     *
     * @param num     数量
     * @param appName 组织名称
     * @param skuId   订阅
     * @return 成功
     */
    @GetMapping("/createUserBatch")
    public BaseResultVo createUserBatch(String appName, Integer num, String skuId, String domain) {
        if (num == null || StringUtils.isBlank(appName) || StringUtils.isBlank(domain)) {
            return BaseResultVo.error("参数为空！");
        }
        if (graphProperties.getConfig(appName) == null) {
            return BaseResultVo.error("组织类型不存在！");
        }
        if (num > NUM) {
            for (int i = 0; i < num / NUM; i++) {

                microsoft365Service.createBatch(NUM, appName, "china", skuId, domain);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            microsoft365Service.createBatch(num, appName, "china", skuId, domain);
        }
        return BaseResultVo.success();
    }


}
