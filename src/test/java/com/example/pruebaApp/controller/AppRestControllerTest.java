package com.example.pruebaApp.controller;

import com.example.pruebaApp.model.Greeting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
class AppRestControllerTest {

    private static final Logger log = LogManager.getLogger(AppRestControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Cargando Data")
    @BeforeEach
    void init() {
        log.info("Inicializando MockMvc para pruebas...");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                //.apply(springSecurity())
                .build();
    }

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    void getAllBares() {

        Greeting greeting = new Greeting(0, "Hello Ibis");

        JsonNode json = objectMapper.valueToTree(greeting);

        try {
            mockMvc.perform(get("/getBar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(greeting))
                            .characterEncoding("utf-8")).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(greeting)))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}