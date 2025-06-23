package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("register")
    public void register(){}

    @PostMapping("register")
    public String register(BoardDTO boardDTO){
        // insert, update, delete => return 1 row (성공한 row 의 개수)
        // jpa insert, update, delete => return id

        Long bno = boardService.insert(boardDTO);
        log.info(">> insert id >> {}", bno);


        return "index";
    }
}
