package com.tecsup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.dto.CategoriaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST POST - Crear categoría
    @Test
    void testGuardarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Deportes");
        dto.setDescripcion("Artículos deportivos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Deportes"))
                .andExpect(jsonPath("$.descripcion").value("Artículos deportivos"));
    }

    // TEST POST - Categoría duplicada (409 Conflict)
    @Test
    void testGuardarCategoriaDuplicada() throws Exception {
        // Crear primera
        CategoriaDTO dto1 = new CategoriaDTO();
        dto1.setNombre("Electrónica");
        dto1.setDescripcion("Dispositivos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        // Intentar crear duplicada
        CategoriaDTO dto2 = new CategoriaDTO();
        dto2.setNombre("Electrónica");
        dto2.setDescripcion("Otra descripción");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict());
    }

    // TEST GET - Listar categorías
    @Test
    void testListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk());
    }

    // TEST GET - Obtener por ID
    @Test
    void testObtenerCategoriaPorId() throws Exception {
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk());
    }

    // TEST GET - Categoría no existe (404)
    @Test
    void testObtenerCategoriaNoExistente() throws Exception {
        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    // TEST PUT - Actualizar categoría
    @Test
    void testActualizarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Tecnología");
        dto.setDescripcion("Dispositivos tecnológicos actualizados");

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnología"))
                .andExpect(jsonPath("$.descripcion").value("Dispositivos tecnológicos actualizados"));
    }

    // TEST PUT - Actualizar categoría no existente (404)
    @Test
    void testActualizarCategoriaNoExistente() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Inexistente");
        dto.setDescripcion("No existe");

        mockMvc.perform(put("/api/categorias/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST PUT - Actualizar con nombre duplicado (409)
    @Test
    void testActualizarCategoriaNombreDuplicado() throws Exception {
        // Crear segunda categoría
        CategoriaDTO dto2 = new CategoriaDTO();
        dto2.setNombre("Hogar");
        dto2.setDescripcion("Artículos hogar");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

        // Intentar actualizar primera categoría con nombre de la segunda
        CategoriaDTO dtoUpdate = new CategoriaDTO();
        dtoUpdate.setNombre("Hogar");
        dtoUpdate.setDescripcion("Duplicado");

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate)))
                .andExpect(status().isConflict());
    }

    // TEST DELETE - Eliminar categoría
    @Test
    void testEliminarCategoria() throws Exception {
        // Crear categoría para eliminar
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Temporal");
        dto.setDescripcion("Para eliminar");

        String response = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Eliminar (usamos ID 1 que existe en data.sql)
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado correctamente"));
    }

    // TEST DELETE - Eliminar categoría no existente (404)
    @Test
    void testEliminarCategoriaNoExistente() throws Exception {
        mockMvc.perform(delete("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    // TEST VALIDACIÓN - Nombre vacío
    @Test
    void testValidacionNombreVacio() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("");
        dto.setDescripcion("Descripción válida");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN - Nombre muy largo
    @Test
    void testValidacionNombreMuyLargo() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("A".repeat(101)); // más de 100 chars
        dto.setDescripcion("Descripción válida");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN - Descripción muy larga
    @Test
    void testValidacionDescripcionMuyLarga() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Válido");
        dto.setDescripcion("A".repeat(256)); // más de 255 chars

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}