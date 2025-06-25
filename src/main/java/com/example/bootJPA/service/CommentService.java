package com.example.bootJPA.service;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.entity.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {

    default Comment convertDtoToEntity(CommentDTO commentDTO) {

        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .content(commentDTO.getContent())
                .writer(commentDTO.getWriter())
                .build();
    }

    default CommentDTO convertEntityToDto(Comment comment) {

        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .modDate(comment.getModDate())
                .regDate(comment.getRegDate())
                .build();
    }

    Long insertComment(CommentDTO commentDTO);

    // List<CommentDTO> getCommentList(Long bno)
    Page<CommentDTO> getCommentList(Long bno, int page);

    Long deleteComment(Long cno);

    Long updateComment(CommentDTO commentDTO);
}
