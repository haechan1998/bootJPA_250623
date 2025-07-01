package com.example.bootJPA.controller;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post")
    public ResponseEntity<String> post(@RequestBody CommentDTO commentDTO) {
        log.info("commentController commentDTO >> {}", commentDTO);

        Long cno = commentService.insertComment(commentDTO);

        return ResponseEntity.ok(cno > 0 ? "1" : "0");

    }

    @GetMapping("/list/{bno}/{page}")
    public ResponseEntity<PagingHandler> getList(
            @PathVariable("bno") Long bno,
            @PathVariable("page") int page
    ){
        // 페이지 없는 경우
        // List<CommentDTO> commentList = commentService.getCommentList(bno);
        
        // 페이지 있는 경우 page = 0 부터
        Page<CommentDTO> list = commentService.getCommentList(bno, page-1);
        PagingHandler<CommentDTO> ph = new PagingHandler(list, page);

        return ResponseEntity.ok(ph);

    }

    @DeleteMapping("/delete/{cno}")
    public ResponseEntity<String> delete(@PathVariable("cno") Long cno){

        Long isOk = commentService.deleteComment(cno);

        return ResponseEntity.ok(isOk != null ? "1" : "0");

    }

    @PutMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody CommentDTO commentDTO){

        log.info(">>> modify commentDTO > {}", commentDTO);

        Long isOk = commentService.updateComment(commentDTO);

        return ResponseEntity.ok(isOk != null ? "1" : "0");
    }

}
