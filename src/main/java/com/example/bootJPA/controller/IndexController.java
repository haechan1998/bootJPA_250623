package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/")
    public String getTop5Lists(Model model){

        // 조회수
        List<BoardDTO> top5BoardList = boardService.getTop5MostViewedBoardList();
        log.info(">>> top5BoardList > {}", top5BoardList);
        // 최근글
        List<BoardDTO> latest5BoardList = boardService.getLatest5BoardList();
        log.info(">>> latest5BoardList > {}", latest5BoardList);

        model.addAttribute("Latest5BoardList", latest5BoardList);
        model.addAttribute("top5BoardList", top5BoardList);

        return "index";
    }
}
