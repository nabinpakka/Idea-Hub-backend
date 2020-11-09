package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.repo.FileDBRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    private FileDBRepo fileDBRepo;

    @Autowired
    public FileStorageService(@Qualifier("file") FileDBRepo fileDBRepo) {
        this.fileDBRepo = fileDBRepo;
    }

    public FileDB store(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(),file.getBytes());
        return (FileDB) fileDBRepo.save(fileDB);
    }

    public FileDB getFile(String id){
        return (FileDB) fileDBRepo.findById(id).get();
    }
}
