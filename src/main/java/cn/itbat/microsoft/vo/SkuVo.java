package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订阅信息
 *
 * @author mjj
 * @date 2020年08月06日 12:52:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuVo {


    private String skuId;

    /**
     * 订阅类型
     */
    private String skuType;

    /**
     * 订阅名称
     */
    private String skuName;
}
