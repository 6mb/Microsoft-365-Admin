package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huahui.wu
 * @date 2020年11月25日 13:13:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FontUser {

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 用户名
     */
    private String userPrincipalName;

    /**
     * 密码
     */
    private String password;
}
