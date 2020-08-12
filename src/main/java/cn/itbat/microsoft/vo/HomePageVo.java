package cn.itbat.microsoft.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 首页展示信息
 *
 * @author mjj
 * @date 2020年08月11日 10:38:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageVo implements Serializable {

    /**
     * 统计信息
     */
    private StatisticsVo statisticsVo;

    /**
     * 禁止登陆用户
     */
    private List<GraphUserVo> noLandingUsers;

    /**
     * 未授权用户
     */
    private List<GraphUserVo> unauthorizedUsers;

}
