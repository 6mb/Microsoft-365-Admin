package cn.itbat.microsoft.vo;

import com.microsoft.graph.models.extensions.DirectoryRole;
import lombok.Data;

@Data
public class DirectoryRoleVo {

    /**
     * 角色描述
     */
    public String description;

    /**
     * 角色名称
     */
    public String displayName;

    /**
     * 角色模板ID
     */
    public String roleTemplateId;

    public DirectoryRoleVo(DirectoryRole role) {
        description = role.description;
        displayName = role.displayName;
        roleTemplateId = role.roleTemplateId;
    }
}
