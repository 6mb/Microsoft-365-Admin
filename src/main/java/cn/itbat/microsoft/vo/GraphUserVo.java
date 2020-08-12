package cn.itbat.microsoft.vo;

import cn.itbat.microsoft.model.GraphUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

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
     * 查询大小，默认999
     */
    @Builder.Default
    private Integer top = 999;

    /**
     * 订阅信息
     */
    private List<SkuVo> skuVos;


    public GraphUserVo() {

    }

    public GraphUserVo(GraphUser graphUser) {
        if (null != graphUser) {
            BeanUtils.copyProperties(graphUser, this);
        }
    }
}
