package com.example.bootJPA.repository;

import com.example.bootJPA.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // List<comment> findByBno(Long bno); // 페이지가 없을 경우
    Page<Comment> findByBno(Long bno, Pageable pageable); // bno 를 주면 해당하는 댓글을 가져오는 쿼리

}
