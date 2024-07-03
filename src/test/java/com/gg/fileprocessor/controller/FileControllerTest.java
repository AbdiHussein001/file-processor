package com.gg.fileprocessor.controller;

import com.gg.fileprocessor.service.FileProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileProcessingService fileProcessingService;

    @Value("${app.skipValidation:false}")
    private boolean skipValidation;

    @Test
    public void testProcessFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "EntryFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n".getBytes()
        );

        when(fileProcessingService.processFile(any(), any(Boolean.class)))
                .thenReturn("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]");

        mockMvc.perform(multipart("/process")
                        .file(mockFile)
                        .param("skipValidation", "true"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"OutcomeFile.json\""))
                .andExpect(content().json("[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]"));
    }

    @Test
    public void testProcessFileInvalid() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "EntryFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "invalid-uuid|1X1D14|John Smith|Likes Apricots|Rides A Bike|invalid|12.1\n".getBytes()
        );

        when(fileProcessingService.processFile(any(), any(Boolean.class)))
                .thenThrow(new IllegalArgumentException("Invalid data in file"));

        mockMvc.perform(multipart("/process")
                        .file(mockFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data in file"));
    }
}