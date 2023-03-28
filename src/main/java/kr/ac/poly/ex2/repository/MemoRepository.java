package kr.ac.poly.ex2.repository;

import kr.ac.poly.ex2.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // select 특정 mno 값의 범위의 값들을 내림차순 정렬
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // select 특정 mno 값의 범위의 값들을 내림차순 정렬 + 페이징처리
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // delete 특정 mno 값보다 작은 행들을 삭제처리 기능
    void deleteMemoByMnoLessThan(Long num);

    //Query 를 사용하여 JPQL을 실행
    //select문 실행
    @Query("select m from Memo m order by m.mno desc ")
    List<Memo> getListDesc();

    //Query 를 사용하여 JPQL을 실행
    // update문 실행(parameter로 JPQL에 값 전달)
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    //Query 를 사용하여 JPQL을 실행
    // update문 실행(#{}과 같이 자바 빈 스타일을 이용하여 JPQL에 값 전달)
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoText2(@Param("param")  Memo memo);

    @Query(value = "select m from Memo m where m.mno > :mno",
        countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable); //쿼리 어노테이션을 통한 이 메소드는 이름을 임의로 가능
}
