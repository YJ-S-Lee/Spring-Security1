package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.security.domain.EzenMember;
import spring.security.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    //로그인
    public EzenMember login(String loginId, String password) {
        Optional<EzenMember> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            EzenMember ezenMember = findMember.get();
            if (ezenMember.getPassword().equals(password)) {
                return ezenMember;
            }
            else return null;
        }
        return null;
    }
}