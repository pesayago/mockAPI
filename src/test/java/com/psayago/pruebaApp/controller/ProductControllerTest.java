package com.psayago.pruebaApp.controller;

import com.psayago.pruebaApp.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("GET /products devuelve los productos sembrados desde data.sql")
    void getAll() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Handmade Candle Set"))
                .andExpect(jsonPath("$[0].price").value("$24.00"));
    }

    @Test
    @DisplayName("GET /products/{id} devuelve un producto existente")
    void getByIdExisting() throws Exception {
        mockMvc.perform(get("/products/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Keepsake Jewelry Box"))
                .andExpect(jsonPath("$.badge").value("Premium"));
    }

    @Test
    @DisplayName("GET /products/{id} inexistente devuelve 404")
    void getByIdNotFound() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /products crea un producto y luego es accesible por id")
    void createAndFetch() throws Exception {
        Product nuevo = new Product(100L, "Test Mug", "$9.99", "/img/TestMug.webp",
                "A simple ceramic mug for testing.", "Test");

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("Test Mug"));

        mockMvc.perform(get("/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("$9.99"));
    }

    @Test
    @DisplayName("DELETE /products/{id} elimina y deja el recurso en 404")
    void deleteExisting() throws Exception {
        mockMvc.perform(delete("/products/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /products/{id} inexistente devuelve 404")
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete("/products/999"))
                .andExpect(status().isNotFound());
    }
}
