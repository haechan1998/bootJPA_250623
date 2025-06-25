package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /*
    @GetMapping("list")
    public void boardList(Model model){
        *//* 페이징 없는 일반 리스트
        List<BoardDTO> list = boardService.getBoardList();

        model.addAttribute("list", list);

    }
    */

//    @GetMapping("/list")
//    public void list(Model model, @RequestParam(name="pageNo", defaultValue = "1", required = false) int pageNo){ // 필수가 아닐경우 required=false
//        paging 만 포함
//        /* select * from board order by bno desc limit 0, 10 */
//        /* limit 시작번지, 개수 => 시작번지는 0부터 시작 */
//        int pageNo = 1; // 나중에 페이지네이션 클릭하는 값으로 입력 될 값
//
//        Page<BoardDTO> list = boardService.getPageBoardList(pageNo-1);
//
//        log.info(">>>> Page Info >>>>");
//        log.info("총 요소 수 (total elements): {}", list.getTotalElements());
//        log.info("총 페이지 수 (total pages): {}", list.getTotalPages());
//        log.info("현재 페이지 번호 (current page): {}", list.getNumber());
//        log.info("현재 페이지 요소 수: {}", list.getNumberOfElements());
//        log.info("페이지 크기 (page size): {}", list.getSize());
//        log.info("첫 페이지 여부: {}", list.isFirst());
//        log.info("마지막 페이지 여부: {}", list.isLast());
//        log.info("정렬 정보: {}", list.getSort());
//        log.info("페이지 콘텐츠 목록: {}", list.getContent()); // List<BoardDTO>
//
//        PagingHandler pagingHandler = new PagingHandler(list, pageNo);
//
//        model.addAttribute("list", list);
//        model.addAttribute("ph", pagingHandler);
//
//    }

    @GetMapping("list")
    public void boardList(
            Model model,
            @RequestParam(name = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "keyword", required = false) String keyword
    ){
        /* Page + search 포함 */
        int page = pageNo - 1;
        Page<BoardDTO> list = boardService.getList(page, type, keyword);
        PagingHandler<BoardDTO> pagingHandler = new PagingHandler(list, pageNo, type, keyword);
        log.info(">>>> list total > {}", list.getTotalElements());
        // model.addAttribute("list", list);
        model.addAttribute("ph", pagingHandler);

    }

    @GetMapping("detail")
    public void boardDetail(Model model, @RequestParam("bno") Long bno){

        log.info(">>> bno > {}",bno);
        BoardDTO boardDTO = boardService.getDetail(bno);

        model.addAttribute("boardDTO", boardDTO);

    }

    @GetMapping("remove")
    public String boardRemove(@RequestParam("bno") Long bno){

        boardService.boardDelete(bno);

        return "redirect:/board/list";
    }

    @PostMapping("modify")
    public String boardModify(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        boardService.boardModify(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/detail";
    }








}
