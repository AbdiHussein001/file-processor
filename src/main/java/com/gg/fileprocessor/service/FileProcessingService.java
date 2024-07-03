package com.gg.fileprocessor.service;

import com.gg.fileprocessor.model.Outcome;
import com.gg.fileprocessor.util.FileValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileProcessingService {

    private final ObjectMapper objectMapper;

    public FileProcessingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String processFile(MultipartFile file, boolean skipValidation) throws IOException {
        List<Outcome> outcomes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 7) {
                    throw new IllegalArgumentException("Invalid file format");
                }

                if (!skipValidation && !FileValidator.isValid(parts)) {
                    throw new IllegalArgumentException("Invalid data in file");
                }

                Outcome outcome = new Outcome();
                outcome.setName(parts[2]);
                outcome.setTransport(parts[4]);
                outcome.setTopSpeed(Double.parseDouble(parts[6]));

                outcomes.add(outcome);
            }
        }

        return objectMapper.writeValueAsString(outcomes);
    }
}
