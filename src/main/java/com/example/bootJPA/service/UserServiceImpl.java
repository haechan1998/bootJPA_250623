package com.example.bootJPA.service;

import com.example.bootJPA.dto.UserDTO;
import com.example.bootJPA.entity.AuthUser;
import com.example.bootJPA.entity.User;
import com.example.bootJPA.repository.AuthUserRepository;
import com.example.bootJPA.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthUserRepository authUserRepository;

    @Override
    public String insertUser(UserDTO userDTO) {

        String email = userRepository.save(convertDtoToEntity(userDTO)).getEmail();
        if(email != null){
            authUserRepository.save(convertDtoToAuthEntity(userDTO));
        }

        return email;
    }

    @Override
    public UserDTO selectEmail(String username) {

        // 찾는 검색값이 id 가 아닌 경우 해당 repository 에 등록 후 사용
        // findBy~ => select * from table where ~
        Optional<User> optional = userRepository.findById(username);
        User user = optional.get();
        log.info("UserServiceImpl user >>> {}", user);
        List<AuthUser> authUserList = authUserRepository.findByEmail(username);
        log.info("UserServiceImpl authUserList >>> {}", authUserList);


        
        // List<Entity> 인 값을 List<DTO> 로 변경 할 경우 stream.map(this :: 변환메서드명).toList() 를 사용하면 변환 가능
        if(optional.isPresent()){
            log.info("123412341234");
            UserDTO userDTO = convertEntityToDto(user,
                    authUserList.stream()
                            .map(this::convertEntityToAuthDto)
                            .toList()
                    );
            return userDTO;
        }
        // optional 값이 없으면 null 리턴

        return null;
    }

    @Transactional
    @Override
    public boolean updateLastLogin(String name) {

        Optional<User> optional = userRepository.findById(name);
        if(optional.isPresent()){
            User user = optional.get();
            user.setLastLogin(LocalDateTime.now());

            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public String modify(UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userDTO.getEmail());
        if(optional.isPresent()){
            User user = optional.get();
            log.info("modify Entity user > {}", user);

            // 비밀번호 수정하면 변경
            if(userDTO.getPwd().length() > 0){
                user.setPwd(userDTO.getPwd());
            }
            user.setNickName(userDTO.getNickName());

            return userDTO.getEmail();
        }
        return null;
    }

    @Transactional
    @Override
    public String removeUser(String name) {
        Optional<User> optional = userRepository.findById(name);
        if(optional.isPresent()){
            userRepository.deleteById(name);

            authUserRepository.deleteByEmail(name);
            return name;
        }
        return null;
    }

    @Override
    public Object getList() {

        // user findAll
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user : userList){
            List<AuthUser> authUserList = authUserRepository.findByEmail(user.getEmail());
            UserDTO userDTO = convertEntityToDto(user, authUserList.stream().map(this :: convertEntityToAuthDto).toList());

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }
}
