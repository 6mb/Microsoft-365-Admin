package cn.itbat.microsoft.controller;

import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.model.entity.InvitationCode;
import cn.itbat.microsoft.service.InvitationCodeService;
import cn.itbat.microsoft.vo.BaseResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huahui.wu
 * @date 2020年11月24日 16:15:19
 */
@Slf4j
@RestController
@RequestMapping("/microsoft/code")
public class InvitationCodeController {

    @Resource
    private InvitationCodeService invitationCodeService;

    @GetMapping("/getCodeStatistics")
    public BaseResultVo getCodeStatistics() {
        return BaseResultVo.success(invitationCodeService.getStatistics());
    }

    @GetMapping("/list")
    public BaseResultVo list(InvitationCode invitationCode, Pager pager) {
        return BaseResultVo.success(invitationCodeService.list(invitationCode, pager));
    }

    @PostMapping("/generate")
    public BaseResultVo generate(Integer num) {
        invitationCodeService.generateInvitationCode(num);
        return BaseResultVo.success();
    }
}
