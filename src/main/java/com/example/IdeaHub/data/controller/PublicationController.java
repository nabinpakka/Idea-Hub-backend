package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.FileStorageService;
import com.example.IdeaHub.data.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PublicationService publicationService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("abstract") String abs,
                                                      @RequestParam("reviewScore") Integer reviewScore,
                                                      @RequestParam("detail") String detail,
                                                      @RequestParam("publicationHouse") String publicationHouse,
                                                      @RequestParam("authorId") String authorId){
        String message = "";
        try{
            FileDB fileDB = fileStorageService.store(file);
            String fileId = fileDB.getId();
            System.out.println(fileId);

            //creating new publication object
            Publication publication = new Publication(
                    authorId,title,abs,detail,reviewScore,publicationHouse,fileId
            );
            //saving publication
            publicationService.upload(publication);

            System.out.println(fileDB);
            message = "Uploaded Successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
            message = "Could not upload the file: " ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("{id}")
    public Optional<Publication> getPublication(@PathVariable String id){
        return publicationService.getPublication(id);

    }

    //getting file by id
    @GetMapping("/fileDownload/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        FileDB fileDB = fileStorageService.getFile(id);
        System.out.println(fileDB);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

}
