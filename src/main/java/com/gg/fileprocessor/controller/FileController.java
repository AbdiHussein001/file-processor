package com.gg.fileprocessor.controller;

import com.gg.fileprocessor.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/process")
public class FileController {

    private final FileProcessingService fileProcessingService;

    @Value("${app.skipValidation:false}")
    private boolean skipValidation;

    public FileController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping
    public ResponseEntity<?> processFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "skipValidation", required = false) Boolean skipValidationFlag) {
        try {
            boolean skip = (skipValidationFlag != null) ? skipValidationFlag : skipValidation;
            String result = fileProcessingService.processFile(file, skip);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "OutcomeFile.json");

            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
