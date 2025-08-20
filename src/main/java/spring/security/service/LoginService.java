package spring.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.security.domain.EzenMember;
import spring.security.domain.UserRoll;
import spring.security.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<EzenMember> findLoginId = memberRepository.findByLoginId(loginId);
        if(findLoginId.isEmpty()) {
            throw  new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        EzenMember findezenMember = findLoginId.get();
        log.info("꺼낸 맴버 {}", findezenMember);
        //권한 부여
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if("user".equals(findezenMember.getGrade())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(UserRoll.USER.getValue()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(UserRoll.ADMIN.getValue()));
        }
        log.info("찍어봐 {} {} {}", findezenMember.getLoginId(), findezenMember.getPassword(), grantedAuthorities);
        return new User(findezenMember.getLoginId(), findezenMember.getPassword(), grantedAuthorities);
    }
}