package com.TTLTTBDD.server.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class loadFile {
    public loadFile(){}

    @Value("${upload.dir}")
    private String uploadDir="D:/uploads/";
    public String saveFile(MultipartFile File) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName =File.getOriginalFilename();
        File file = new File(uploadDir + fileName);
        File.transferTo(file);
        return fileName;
    }
}
