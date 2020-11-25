package cn.itbat.microsoft.service.impl;

import cn.itbat.microsoft.config.GraphProperties;
import cn.itbat.microsoft.model.entity.InvitationCode;
import cn.itbat.microsoft.service.FrontDeskService;
import cn.itbat.microsoft.service.InvitationCodeService;
import cn.itbat.microsoft.service.Microsoft365Service;
import cn.itbat.microsoft.vo.GraphUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author huahui.wu
 * @date 2020年11月24日 16:34:12
 */
@Service
public class FrontDeskServiceImpl implements FrontDeskService {

    @Resource
    private Microsoft365Service microsoft365Service;

    @Resource
    private InvitationCodeService invitationCodeService;

    @Resource
    private GraphProperties graphProperties;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(GraphUserVo graphUserVo) {
        // 判断邀请码是否有效
        InvitationCode invitationCode = invitationCodeService.selectByCode(graphUserVo.getCode());
        if (invitationCode == null || !invitationCode.getValid()) {
            throw new RuntimeException("邀请码已失效！");
        }
        GraphUserVo userVo = microsoft365Service.create(graphUserVo);
        invitationCode.setValid(Boolean.FALSE);
        invitationCode.setExpirationTime(new Date());
        invitationCode.setInvitedUser(userVo.getUserPrincipalName());
        invitationCode.setSubscribe(StringUtils.isEmpty(graphUserVo.getSkuType()) ? "" : graphProperties.getSubConfig(graphUserVo.getSkuType()).getDisplayName());
        invitationCodeService.update(invitationCode);
    }
}
