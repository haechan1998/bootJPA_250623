package com.example.bootJPA.repository;

import com.example.bootJPA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // 닉네임 중복검사용
    Optional<User> findByNickName(String nickName);

}
