package cn.itbat.microsoft.service;

import cn.itbat.microsoft.vo.GraphUserVo;

/**
 * @author huahui.wu
 * @date 2020年11月24日 16:34:06
 */
public interface FrontDeskService {

    /**
     * 创建用户
     *
     * @param graphUserVo 用户信息
     */
    void create(GraphUserVo graphUserVo);
}
