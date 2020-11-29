package com.example.IdeaHub.data.service.implementations;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.repo.FileDBRepo;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.message.ResponseFile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileDBRepo fileDBRepo;

    public FileStorageServiceImpl(@Qualifier("file") FileDBRepo fileDBRepo) {
        this.fileDBRepo = fileDBRepo;
    }

    public FileDB storeFile(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = new FileDB(fileName, file.getContentType(),file.getBytes());
        return fileDBRepo.save(fileDB);
    }

    public FileDB getFile(String id){
        return fileDBRepo.findById(id).get();
    }

    public ResponseEntity<ResponseFile> getFileResponse(String id){
        if (fileDBRepo.findById(id).isPresent()){
            FileDB fileDB = fileDBRepo.findById(id).get();
            String fileDownloadUri  = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(fileDB.getId())
                    .toUriString();

            ResponseFile responseFile = new ResponseFile(
                    fileDB.getName(),
                    fileDownloadUri,
                    fileDB.getType(),
                    fileDB.getData().length
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseFile);
        }
        return null;
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
