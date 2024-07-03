package com.gg.fileprocessor.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.fileprocessor.model.Outcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileProcessingServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FileProcessingService fileProcessingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessFileValidData() throws IOException {
        String fileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));

        Outcome outcome = new Outcome();
        outcome.setName("John Smith");
        outcome.setTransport("Rides A Bike");
        outcome.setTopSpeed(12.1);

        when(objectMapper.writeValueAsString(any())).thenReturn("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]");

        String result = fileProcessingService.processFile(file, false);
        assertEquals("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]", result);
    }

    @Test
    public void testProcessFileInvalidData() {
        String fileContent = "invalid-uuid|1X1D14|John Smith|Likes Apricots|Rides A Bike|invalid|12.1\n";
        MultipartFile file = mock(MultipartFile.class);
        try {
            when(file.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
            assertThrows(IllegalArgumentException.class, () -> fileProcessingService.processFile(file, false));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testProcessFileSkipValidation() throws IOException {
        String fileContent = "invalid-uuid|1X1D14|John Smith|Likes Apricots|Rides A Bike|invalid|12.1\n";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));

        Outcome outcome = new Outcome();
        outcome.setName("John Smith");
        outcome.setTransport("Rides A Bike");
        outcome.setTopSpeed(12.1);

        when(objectMapper.writeValueAsString(any())).thenReturn("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]");

        String result = fileProcessingService.processFile(file, true);
        assertEquals("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]", result);
    }

}