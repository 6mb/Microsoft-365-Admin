package cn.itbat.microsoft.service;

import cn.itbat.microsoft.vo.FontSkuSku;
import cn.itbat.microsoft.vo.FontUser;
import cn.itbat.microsoft.vo.GraphUserVo;

import java.util.List;

/**
 * @author huahui.wu
 * @date 2020年11月24日 16:34:06
 */
public interface FrontDeskService {

    /**
     * 创建用户
     *
     * @param graphUserVo 用户信息
     * @return FontUser
     */
    FontUser create(GraphUserVo graphUserVo);

    /**
     * 查询用户的订阅
     *
     * @return 成功
     */
    List<FontSkuSku> listLicense();
}
