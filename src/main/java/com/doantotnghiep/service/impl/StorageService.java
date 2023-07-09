package com.doantotnghiep.service.impl;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    public void saveFileToServer(MultipartFile multipartFile,String dir,String fileName) throws IOException {
        Path path = Paths.get(dir);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        InputStream inputStream = multipartFile.getInputStream();
        try{
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            inputStream.close();
        }
    }

    public void downloadTemplateFile(HttpServletResponse response,String fileName) {
        try{
            File file = ResourceUtils.getFile("classpath:static/download/"+fileName);
            byte[] data = FileUtils.readFileToByteArray(file);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition","attachment; filename="+file.getName());
            response.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            FileCopyUtils.copy(inputStream,response.getOutputStream());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
