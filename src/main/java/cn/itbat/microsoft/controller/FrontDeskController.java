package cn.itbat.microsoft.controller;

import cn.itbat.microsoft.config.GraphProperties;
import cn.itbat.microsoft.service.FrontDeskService;
import cn.itbat.microsoft.vo.BaseResultVo;
import cn.itbat.microsoft.vo.GraphUserVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 前台
 *
 * @author huahui.wu
 * @date 2020年11月24日 16:32:52
 */
@RestController
@RequestMapping("/front")
public class FrontDeskController {

    @Resource
    private FrontDeskService frontDeskService;

    @Resource
    private GraphProperties graphProperties;

    @GetMapping("/listLicense")
    public BaseResultVo listLicense() {
        return BaseResultVo.success(frontDeskService.listLicense());
    }

    @PostMapping("/create")
    public BaseResultVo createUser(GraphUserVo graphUserVo) {

        // 参数判断
        if (graphUserVo == null) {
            return BaseResultVo.error("参数为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getAppName())) {
            return BaseResultVo.error("参数为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getCode())) {
            return BaseResultVo.error("邀请码为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getSkuId())) {
            return BaseResultVo.error("订阅为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getPassword())) {
            return BaseResultVo.error("密码为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getMailNickname())) {
            return BaseResultVo.error("邮箱前缀为空！");
        }
        if (StringUtils.isEmpty(graphUserVo.getDisplayName())) {
            return BaseResultVo.error("用户名为空！");
        }
        return BaseResultVo.success(frontDeskService.create(graphUserVo));
    }

    /**
     * 查询可注册地区
     *
     * @param appName 组织类型
     * @return 地区
     */
    @GetMapping("/listUsageLocation")
    public BaseResultVo listUsageLocation(String appName) {
        return BaseResultVo.success(graphProperties.listUsageLocation(appName));
    }
}
