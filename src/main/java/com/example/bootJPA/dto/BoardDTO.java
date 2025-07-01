package com.example.bootJPA.dto;

import lombok.*;

import java.time.LocalDateTime;

import static com.example.bootJPA.dto.TimeConverter.detailTimeOrDate;
import static com.example.bootJPA.dto.TimeConverter.timeOrDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regDate, modDate;
    private int fileQty, cmtQty, readCount;

    public String getRegTimeOrDate(){
        return timeOrDate(regDate);
    }

    public String getModTimeOrDate(){
        if(regDate.equals(modDate)){
            return "-";
        }
        return timeOrDate(modDate);
    }

    public String getDetailRegTime(){
        return detailTimeOrDate(regDate);
    }

    public String getDetailModTime(){
        if(regDate.equals(modDate)){
            return "-";
        }
        return detailTimeOrDate(modDate);
    }

}

