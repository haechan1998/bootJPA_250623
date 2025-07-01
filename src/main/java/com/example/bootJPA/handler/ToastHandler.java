package com.example.bootJPA.handler;

import com.example.bootJPA.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class ToastHandler {

    private final String UP_DIR = "D:\\web_java_chc\\_myProject\\_java\\_fileUpload";

    public String imageUpload(MultipartFile image) {
        if(image.isEmpty()){
            return "";
        }

        log.info("toastHandler image >> {}",image);

        LocalDate date = LocalDate.now();
        String today = date.toString().replace("-", File.separator);
        File folders = new File(UP_DIR, today);

        if(!folders.exists()){
            folders.mkdirs();
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSaveDir(today);
        fileDTO.setFileSize(image.getSize());
        String fileDTOName = image.getOriginalFilename();
        fileDTO.setFileName(fileDTOName);
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        fileDTO.setUuid(uuidStr);

        int type = image.getContentType().startsWith("image") ? 1 : 0;
        fileDTO.setFileType(type);

        String fileName = uuidStr+"_"+fileDTOName;
        
        // 파일 저장
        File storeFile = new File(folders, fileName);

        try {
            image.transferTo(storeFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        // /upload/2025/07/01/f5fada4c-1e94-4b0d-b56b-91f5b9d62efa_naver.png 만들어야 하는 경로
//        String replaceStoreFile = storeFile.getPath().replace(File.separator, "/");
//        log.info("replaceStoreFile >>>>>>> {}",replaceStoreFile);
        String toastFileName = today.replace("\\", "/") + "/" + fileName;
        log.info("toastFileName >>>> {}", toastFileName);

        return "/upload/" + toastFileName;

    }

}


