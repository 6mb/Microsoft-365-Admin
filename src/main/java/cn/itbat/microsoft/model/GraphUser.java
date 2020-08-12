package cn.itbat.microsoft.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * office 365 用户
 *
 * @author mjj
 * @date 2020年05月12日 15:59:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphUser {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订阅
     */
    private String skuId;

    /**
     * 订阅类型
     */
    private String skuType;


    /**
     * 域名
     */
    private String domain;

    /**
     * 姓
     */
    private String surname;

    /**
     * 姓
     */
    private String givenName;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 邮箱前缀
     */
    private String mailNickname;

    /**
     * 用户名
     */
    private String userPrincipalName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码类型
     */
    @Builder.Default
    private String passwordPolicies = "DisablePasswordExpiration, DisableStrongPassword";

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 位置
     */
    @Builder.Default
    private String usageLocation = "CN";

    /**
     * 街道地址
     */
    private String streetAddress;

    /**
     * 城市
     */
    private String city;

    /**
     * 省
     */
    private String state;

    /**
     * 国家
     */
    private String country;

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean accountEnabled = true;

    /**
     * 用户创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdDateTime;


}
