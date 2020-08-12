package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 绑定域名
 *
 * @author mjj
 * @date 2020年08月11日 14:57:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainVo implements Serializable {

    /**
     * 域名
     */
    private String id;

    private Boolean isDefault;

    /**
     * 是否是默认域名
     */
    private String displayIsDefault;

    private Boolean isRoot;

    /**
     * 是否是根域名
     */
    private String displayIsRoot;
}
