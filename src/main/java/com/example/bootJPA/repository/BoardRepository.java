package com.example.bootJPA.repository;

/* JpaRepository<테이블명, id의 Class-Type> */

import com.example.bootJPA.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    /* 다른 조건으로 값을 검색 할 때 이렇게 직접 만들어서 사용 가능하다. */
    List<Board> findByWriter(String writer);

    // ?뒤에 숫자는 파라미터 순서
    @Query("select b from Board b where b.writer = ?1") // 테이블 명은 Entity 클래스로 해야한다.
    List<Board> findByWriterQuery(String writer);


}
