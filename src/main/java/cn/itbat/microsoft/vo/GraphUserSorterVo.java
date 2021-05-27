package cn.itbat.microsoft.vo;

import lombok.Data;

@Data
public class GraphUserSorterVo {
    /**
     * 两个字母的国家/地区代码（ISO 标准 3166）。检查服务在国家/地区的可用性
     */
    private String sortusageLocation;

    /**
     * 用户创建时间
     */
    private String sortcreatedDateTime;

    /**
     * 用户名
     */
    private String sortuserPrincipalName;

    /**
     * 角色
     */
    private String sortroles;

}
