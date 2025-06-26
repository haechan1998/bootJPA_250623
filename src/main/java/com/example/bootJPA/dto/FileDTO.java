package com.example.bootJPA.dto;

import lombok.*;

import java.time.LocalDateTime;

import static com.example.bootJPA.dto.TimeConverter.timeOrDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private String uuid;
    private String saveDir;
    private String fileName;
    private int fileType;
    private long bno;
    private long fileSize;
    private LocalDateTime regDate, modDate;

    public String getRegTimeOrDate(){
        return timeOrDate(regDate);
    }

    public String getModTimeOrDate(){
        if(regDate.equals(modDate)){
            return "-";
        }
        return timeOrDate(modDate);
    }

}
