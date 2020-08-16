package cn.itbat.microsoft.controller;


import cn.itbat.microsoft.model.support.SystemMonitorInfo;
import cn.itbat.microsoft.vo.BaseResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统监控 Controller
 *
 * @author mjj
 * @date 2020年08月11日 09:50:37
 */
@RestController
@RequestMapping("/system")
public class SystemController {


    /**
     * 获取系统监控信息
     */
    @GetMapping("/monitor")
    public BaseResultVo monitor() {
        return BaseResultVo.success(new SystemMonitorInfo());
    }

}