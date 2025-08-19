package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.domain.EzenMember;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<EzenMember, Long> {

    //로그인 아이디로 조회하는 기능을 추가
    Optional<EzenMember> findByLoginId(String loginId);
}