package cn.itbat.microsoft.service;

import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.model.entity.InvitationCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 邀请码
 *
 * @author huahui.wu
 * @date 2020年11月24日 15:17:45
 */
public interface InvitationCodeService {
    /**
     * 根据code查询（有效的邀请码）
     *
     * @param code 邀请码
     * @return InvitationCode
     */
    InvitationCode selectByCode(String code);

    /**
     * 更新邀请码
     *
     * @param invitationCode 邀请码信息
     */
    void update(InvitationCode invitationCode);

    /**
     * 生成邀请码
     *
     * @param num 数量
     */
    void generateInvitationCode(Integer num);

    /**
     * 查询
     *
     * @param invitationCode 查询参数
     * @return List<InvitationCode>
     */
    List<InvitationCode> list(InvitationCode invitationCode);

    /**
     * 分页查询
     *
     * @param invitationCode 查询参数
     * @param pager          分页参数
     * @return Page<InvitationCode>
     */
    Page<InvitationCode> list(InvitationCode invitationCode, Pager pager);

    /**
     * 统计信息
     *
     * @return 结果
     */
    Map<String, Object> getStatistics();
}
