package cn.itbat.microsoft.repository;

import cn.itbat.microsoft.model.entity.InvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huahui.wu
 * @date 2020年11月24日 14:57:25
 */
@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCode, Integer> {

    /**
     * 根据code查询
     *
     * @param code  邀请码
     * @param valid 是否有效
     * @return InvitationCode
     */
    InvitationCode findFirstByCodeAndValid(String code, Boolean valid);

    /**
     * 批量查询code
     *
     * @param codes 邀请码集合
     * @return List<InvitationCode>
     */
    List<InvitationCode> findAllByCodeIn(List<String> codes);
}
