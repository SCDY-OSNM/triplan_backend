package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.planservice.entity.Member;
import scdy.planservice.exception.MemberNotFoundException;
import scdy.planservice.repository.queryDsl.MemberCustomRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    default Member findByIdOrElseThrow(Long memberId){
        return findById(memberId).orElseThrow(
                ()-> new MemberNotFoundException("멤버를 찾을 수 없습니다."));
    }
}
