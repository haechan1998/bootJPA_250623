package com.example.bootJPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Comment extends TimeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increments;
    private Long cno;

    @Column(nullable = false)
    private Long bno;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(length = 1000, nullable = false)
    private String content;

    /*

    이런 형식으로 생정자를 만들어서 쓸 수도 있다.

    public Comment (CommentDTO commentDTO) {
        this.cno = commentDTO.getCno();
        this.bno = commentDTO.getBno();
        this.writer = commentDTO.getWriter();
        this.content = commentDTO.getContent();

    }
    */
}
