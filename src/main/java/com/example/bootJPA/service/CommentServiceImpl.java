package com.example.bootJPA.service;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.entity.Board;
import com.example.bootJPA.entity.Comment;
import com.example.bootJPA.repository.BoardRepository;
import com.example.bootJPA.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public Long insertComment(CommentDTO commentDTO) {
        log.info("commentServiceImpl commentDTO >> {}", commentDTO);
        Board board = boardRepository.findById(commentDTO.getBno()).orElseThrow(() -> new EntityNotFoundException());
        
        // 댓글 등록 시 board 에 있는 댓글 수 하나 증가
        board.setCmtQty(board.getCmtQty()+1);
        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public Page<CommentDTO> getCommentList(Long bno, int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);
        return list.map(this :: convertEntityToDto);

//        List<Comment> commentList = commentRepository.findByBno(bno);
//
//        List<CommentDTO> commentDTOList = commentList.stream()
//                .map(this :: convertEntityToDto)
//                .toList();
//
//        return commentDTOList;
    }

    @Transactional
    @Override
    public Long deleteComment(Long cno) {
//        Comment comment = commentRepository.findById(cno).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글"));
        Optional<Comment> optional = commentRepository.findById(cno);
        if(optional.isPresent()){
            commentRepository.deleteById(cno);
            Board board = boardRepository.findById(optional.get().getBno()).orElseThrow(() -> new EntityNotFoundException());
            board.setCmtQty(board.getCmtQty() - 1);
            return cno;
        }
        return null;
    }

    @Override
    public Long updateComment(CommentDTO commentDTO) {

        Optional<Comment> optional = commentRepository.findById(commentDTO.getCno());

        if(optional.isPresent()){
            Comment comment = optional.get();
            comment.setContent(commentDTO.getContent());
            return commentRepository.save(comment).getCno();
        }

        return null;
    }


}
