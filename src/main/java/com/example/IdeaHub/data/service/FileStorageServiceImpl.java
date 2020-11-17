package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.repo.FileDBRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    private final FileDBRepo fileDBRepo;

    public FileStorageServiceImpl(@Qualifier("file") FileDBRepo fileDBRepo) {
        this.fileDBRepo = fileDBRepo;
    }

    public FileDB store(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = new FileDB(fileName, file.getContentType(),file.getBytes());
        return fileDBRepo.save(fileDB);
    }

    public FileDB getFile(String id){
        return fileDBRepo.findById(id).get();
    }

    public int deleteFile(String id) {

        try{
            fileDBRepo.deleteById(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
