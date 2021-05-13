package cn.itbat.microsoft.vo;

import cn.itbat.microsoft.model.GraphUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mjj
 * @date 2020年06月12日 18:29:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GraphUserVo extends GraphUser {

    /**
     * 组织类型
     */
    private String appName;


    /**
     * 是否需要删除一个许可证
     */
    private Boolean needDeleted;

    /**
     * 用户状态
     */
    private String displayAccountEnable;

    /**
     * 订阅名称
     */
    private String skuName;

    /**
     * 是否分配许可证
     */
    private Boolean assignLicense;

    /**
     * 是否发送邮件
     */
    private Boolean sendMail;

    /**
     * 邮箱
     */
    private String mailbox;

    /**
     * 邀请码
     */
    private String code;

    /**
     * 查询大小，默认999
     */
    @Builder.Default
    private Integer top = 999;

    /**
     * 订阅信息
     */
    private List<SkuVo> skuVos;

    /**
     * 两个字母的国家/地区代码（ISO 标准 3166）。检查服务在国家/地区的可用性
     */
    private String usageLocation;

    /**
     * 用户角色
     */
    private List<DirectoryRoleVo> directoryRoles;

    public GraphUserVo() {
        directoryRoles = new ArrayList<>();
    }

    public GraphUserVo(GraphUser graphUser) {
        if (null != graphUser) {
            BeanUtils.copyProperties(graphUser, this);
        }
    }
}
