package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统计信息
 *
 * @author mjj
 * @date 2020年08月11日 10:40:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVo implements Serializable {

    /**
     * 产品订阅
     */
    @Builder.Default
    private Integer productSubs = 0;

    /**
     * 许可证数
     */
    @Builder.Default
    private Integer licenses = 0;

    /**
     * 已分配许可证数
     */
    @Builder.Default
    private Integer allocatedLicenses = 0;

    /**
     * 可用许可证数
     */
    @Builder.Default
    private Integer availableLicenses = 0;

    /**
     * 用户数
     */
    @Builder.Default
    private Integer users = 0;

    /**
     * 允许登陆用户数
     */
    @Builder.Default
    private Integer allowedUsers = 0;

    /**
     * 禁止登陆用户数
     */
    @Builder.Default
    private Integer biddenUsers = 0;

    /**
     * 未授权用户数
     */
    @Builder.Default
    private Integer unauthorizedUsers = 0;
}
