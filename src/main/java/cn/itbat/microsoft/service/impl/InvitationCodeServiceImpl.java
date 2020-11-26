package cn.itbat.microsoft.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.itbat.microsoft.model.Pager;
import cn.itbat.microsoft.model.entity.InvitationCode;
import cn.itbat.microsoft.repository.InvitationCodeRepository;
import cn.itbat.microsoft.service.InvitationCodeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 邀请码
 *
 * @author huahui.wu
 * @date 2020年11月24日 15:18:03
 */
@Service
public class InvitationCodeServiceImpl implements InvitationCodeService {

    @Resource
    private InvitationCodeRepository invitationCodeRepository;

    @Override
    public InvitationCode selectByCode(String code) {
        return invitationCodeRepository.findFirstByCodeAndValid(code, Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(InvitationCode invitationCode) {
        invitationCodeRepository.save(invitationCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateInvitationCode(Integer num) {
        List<InvitationCode> invitationCodes = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String randomString = "G-" + DateUtil.thisDayOfMonth() + "-" + RandomUtil.randomStringUpper(6);
            invitationCodes.add(InvitationCode.builder().code(randomString).createTime(new Date()).valid(Boolean.TRUE).build());
        }
        List<InvitationCode> codes = invitationCodeRepository.findAllByCodeIn(invitationCodes.stream().map(InvitationCode::getCode).collect(Collectors.toList()));
        if (!CollectionUtils.isEmpty(codes)) {
            List<InvitationCode> userDelete = invitationCodes.stream().filter(l -> codes.stream().map(InvitationCode::getCode).collect(Collectors.toList()).contains(l.getCode())).collect(Collectors.toList());
            invitationCodeRepository.saveAll(userDelete);
        } else {
            invitationCodeRepository.saveAll(invitationCodes);
        }
    }

    @Override
    public List<InvitationCode> list(InvitationCode invitationCode) {
        return invitationCodeRepository.findAll(Example.of(invitationCode));
    }

    @Override
    public Page<InvitationCode> list(InvitationCode invitationCode, Pager pager) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isEmpty(invitationCode.getCode())) {
            invitationCode.setCode(null);
        }
        return invitationCodeRepository.findAll(Example.of(invitationCode, ExampleMatcher.matching()
                .withIgnoreNullValues()), PageRequest.of(pager.getPageIndex() - 1, pager.getPageSize(), sort));
    }

    @Override
    public Map<String, Object> getStatistics() {
        List<InvitationCode> all = invitationCodeRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return new HashMap<>(0);
        }
        Map<String, Object> result = new HashMap<>(3);
        result.put("codes", all.size());
        result.put("valid", all.stream().filter(InvitationCode::getValid).count());
        result.put("invalid", all.stream().filter(l -> !l.getValid()).count());
        result.put("users", all.stream().filter(l -> l.getInvitedUser() != null).count());
        return result;
    }


}
