package com.example.bootJPA.entity;

import jakarta.persistence.*;
import lombok.*;

/*
* Entity : DB 의 테이블 클래스
* VO 개념 대신 DTO
* DTO : 객체를 생성하는 클래스
*
* JPA Auditing : reg_date, mod_date 등록일과, 수정일 같은
* 모든 클래스에서 동일하게 사용되는 칼럼을 별도로 관리 => base class 별도 관리
*
* @Id = primary key
* 기본기 생성 전략 : GeneratedValue
* auto_increments => GeneratedType.IDENTITY
*
* @Column(설정=값) => 생략가능
*
* */

// 일반적으로 테이블 명을 클래스명으로 설정 => @Table 을 설정하면 테이블 이름 변경 가능
@Table(name = "board")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends TimeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 200, nullable = false) // => varchar(200) not null
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(name = "read_count", columnDefinition = "integer default 0")
    private int readCount;

    @Column(name = "cmt_qty", columnDefinition = "integer default 0")
    private int cmtQty;

    @Column(name = "file_qty", columnDefinition = "integer default 0")
    private int fileQty;

}
