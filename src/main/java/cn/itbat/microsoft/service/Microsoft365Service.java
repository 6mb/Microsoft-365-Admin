package cn.itbat.microsoft.service;


import cn.itbat.microsoft.model.GraphUser;
import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.utils.PageInfo;
import cn.itbat.microsoft.vo.*;

import java.util.List;

/**
 * @author mjj   (;￢＿￢)  
 * @version 1.0 
 * @date 2019-07-16 19:06
 **/
public interface Microsoft365Service {

    /**
     * 刷新
     *
     * @param appName 组织类型
     * @param type    刷新类型
     */
    void refresh(String appName, Integer type);

    /**
     * 查询订阅
     *
     * @param appName 组织类型
     * @return List<SubscribedSku>
     */
    List<SubscribedSkuVo> getSubscribed(String appName);


    /**
     * 查询域名
     *
     * @param appName 组织类型
     * @return List<DomainVo>
     */
    List<DomainVo> getDomainVo(String appName);

    /**
     * 创建用户
     *
     * @param graphUserVo 用户信息
     * @return GraphUser
     */
    GraphUserVo create(GraphUserVo graphUserVo);

    /**
     * 更新用户信息
     *
     * @param appName   组织类型
     * @param graphUser 用户
     */

    void updateUser(String appName, GraphUser graphUser);

    /**
     * 启用禁用账户
     *
     * @param appName        组织类型
     * @param userId         用户
     * @param accountEnabled 启用禁用
     */
    void enableDisableUser(String appName, String userId, Boolean accountEnabled);

    /**
     * 添加订阅
     *
     * @param appName 组织类型
     * @param userId  用户
     * @param skuId   订阅
     * @return 用户信息
     */
    GraphUserVo addLicense(String appName, String userId, String skuId);


    /**
     * 取消订阅
     *
     * @param appName 组织类型
     * @param skuId   订阅
     * @param userId  用户
     * @return 用户信息
     */
    GraphUserVo cancelLicense(String appName, String userId, String skuId);

    /**
     * 重置密码
     *
     * @param appName  组织类型
     * @param userName 用户
     * @param password 密码
     */
    void resetPassword(String appName, String userName, String password);


    /**
     * 根据用户Id获取
     *
     * @param appName 组织类型
     * @param userId  用户
     * @return user
     */
    GraphUserVo getGraphUserVo(String appName, String userId);


    /**
     * 获取所有的用户
     *
     * @param appName 组织类型
     * @param pager   分页参数
     * @return List<GraphUserVo>
     */
    PageInfo<GraphUserVo> getGraphUserVos(String appName, Pager pager);

    /**
     * 根据用户id获取用户
     *
     * @param graphUserVo 查询条件
     * @param pager       分页参数
     * @return List<User>
     */
    PageInfo<GraphUserVo> getGraphUserVos(GraphUserVo graphUserVo, Pager pager);


    /**
     * 删除指定用户
     *
     * @param type     组织类型
     * @param userName 用户名
     */
    void deletedUser(String type, String userName);

    /**
     * 删除用户
     *
     * @param appName 组织类型
     */
    void deletedUsers(String appName);


    /**
     * 批量创建创建Office用户
     *
     * @param num     num
     * @param appName 组织类型
     * @param region  国家
     * @param skuId   订阅
     * @param domain  域名后缀
     */
    void createBatch(Integer num, String appName, String region, String skuId, String domain);


    /**
     * 首页
     *
     * @param appName 组织类型
     * @return 首页数据
     */
    HomePageVo homePage(String appName);

    /**
     * 统计许可证信息
     *
     * @param appName 组织类型
     * @return 结果
     */
    StatisticsVo getLicenseStatistics(String appName);

    /**
     * 统计用户信息
     *
     * @param appName 组织类型
     * @return 结果
     */
    StatisticsVo getUsersStatistics(String appName);
}

