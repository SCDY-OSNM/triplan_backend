package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.exception.MemberNotFoundException;
import scdy.planservice.repository.queryDsl.MemberCustomRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    default Member findByIdOrElseThrow(Long memberId){
        return findById(memberId).orElseThrow(
                ()-> new MemberNotFoundException("멤버를 찾을 수 없습니다."));
    }
}
