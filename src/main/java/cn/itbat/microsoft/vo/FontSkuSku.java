package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huahui.wu
 * @date 2020年11月25日 11:57:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FontSkuSku {
    /**
     * 组织名称
     */
    private String appName;

    /**
     * 订阅id
     */
    private String id;

    /**
     * 许可证id
     */
    private String skuId;

    /**
     * 许可证名称
     */
    private String skuName;

}

