package com.example.bootJPA.controller;


import com.example.bootJPA.dto.UserDTO;
import com.example.bootJPA.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/*")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String join(UserDTO userDTO){

        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));

        String email = userService.insertUser(userDTO);

        return (email != null) ? "redirect:/" : "/user/join";
    }

    @GetMapping("/login")
    public String login(
            HttpServletRequest request,
            Model model
                      ){

        String email =(String)request.getSession().getAttribute("email");
        String errorMessage =(String)request.getSession().getAttribute("errorMessage");


        if(errorMessage != null){
            model.addAttribute("email", email);
            model.addAttribute("errorMessage", errorMessage);
            log.info("controller err e > {}",email);
            log.info("controller err m > {}",errorMessage);
        }

        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("errorMessage");


        return "/user/login";
    }

    @GetMapping("modify")
    public void modify(Model model, Principal principal){
        model.addAttribute("userDTO", userService.selectEmail(principal.getName()));

    }

    @PostMapping("/update")
    public String modify(UserDTO userDTO){

        log.info("update userDTO > {}",userDTO);
        if(!userDTO.getPwd().isEmpty()){
            userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        }

        String email = userService.modify(userDTO);

        return "redirect:/user/logout";
    }

    @PostMapping("/remove")
    public String remove(Principal principal){


       String email = userService.removeUser(principal.getName());

       return "redirect:/user/logout";

    }

    @GetMapping("/list")
    public void list(Model model){

        model.addAttribute("userList", userService.getList());
    }

    private void logout(
            HttpServletRequest request,
            HttpServletResponse response
                        ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }


}
