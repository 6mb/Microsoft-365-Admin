package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mjj
 * @date 2020年06月13日 15:56:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribedSkuVo implements Serializable {

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
     * 可取值为：Enabled、Warning、Suspended、Deleted、LockedOut。
     *
     * @see cn.itbat.microsoft.enums.CapabilityStatusEnum
     */
    private String capabilityStatus;

    /**
     * 状态
     */
    private String displayStatus;

    /**
     * 已分配的许可证数量。
     */
    private Integer consumedUnits;

    /**
     * 唯一 SKU 显示名称
     */
    private String skuPartNumber;

    /**
     * 许可证名称
     */
    private String skuName;

    /**
     * 已启用
     */
    private Integer enabled;

    /**
     * 已挂起
     */
    private Integer suspended;

    /**
     * 警告
     */
    private Integer warning;


}
