package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.BoardFileDTO;
import com.example.bootJPA.dto.FileDTO;
import com.example.bootJPA.handler.FileHandler;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.handler.ToastHandler;
import com.example.bootJPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register(){}

    @ResponseBody
    @PostMapping("/register")
    public String register(
            @RequestPart("boardDTO") BoardDTO boardDTO,
            @RequestPart(name = "files", required = false) MultipartFile[] files
    ){
        // 파일이 있는 register

        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
        }
        log.info(">>>> fileList > {}", fileList);
        log.info(">>>> register BoardDTO > {}", boardDTO);
        Long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));
        return bno > 0 ? "1" : "0";

    }

    /*
    // 파일이 없을 경우 register
    @PostMapping("register")
    public String register(BoardDTO boardDTO){
        // insert, update, delete => return 1 row (성공한 row 의 개수)
        // jpa insert, update, delete => return id

        Long bno = boardService.insert(boardDTO);
        log.info(">> insert id >> {}", bno);


        return "index";
    }
    */

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
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);

        model.addAttribute("boardFileDTO", boardFileDTO);

    }

    @GetMapping("remove")
    public String boardRemove(@RequestParam("bno") Long bno){

        boardService.boardDelete(bno);

        return "redirect:/board/list";
    }

    @PostMapping("modify")
    public String boardModify(
            BoardDTO boardDTO,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "files", required = false) MultipartFile[] files
    ){
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            fileList = fileHandler.uploadFiles(files);
        }

        BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileList);

        boardService.boardModify(boardFileDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/detail";
    }

    @ResponseBody
    @DeleteMapping("/file/{uuid}")
    public String fileRemove(@PathVariable("uuid") String uuid){
        long bno = boardService.fileRemove(uuid);
        return bno > 0 ? "1" : "0";
    }

    @GetMapping("/testToast")
    public void testToast(){}

    @ResponseBody
    @PostMapping("/toast")
    public String toastImageUpload(
            @RequestParam MultipartFile image
    ){
        log.info(">>>>>>>>>>>image", image);
        ToastHandler toastHandler = new ToastHandler();
        String dir = toastHandler.imageUpload(image);
        log.info(">>>>>>>>>testToast");
        log.info("toast file 경로 >> {}", dir);

        return dir;
    }

    @ResponseBody
    @PostMapping("/toastPost")
    public String toastPost(
            @RequestBody BoardDTO boardDTO
            ){
        log.info("toast BoardDTO >>>> {}", boardDTO);

        Long isOk = boardService.toastInsert(boardDTO);

        return isOk > 0 ? "1" : "0";
    }

    @GetMapping("/toastView")
    public void toastView(){}

    @ResponseBody
    @PutMapping("/toastModify")
    public String toastModify(@RequestBody BoardDTO boardDTO){

        log.info("toastModify boardDTO >>>>> {}", boardDTO);
        Long isOk = boardService.toastModify(boardDTO);

        return isOk > 0 ? "1" : "0";
    }








}
