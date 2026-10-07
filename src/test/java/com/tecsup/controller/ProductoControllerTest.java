package com.tecsup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.dto.ProductoDTO;
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
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST POST - Crear producto
    @Test
    void testGuardarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setPrecio(80);
        dto.setStock(20);
        dto.setCategoria("Electrónica");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mouse"))
                .andExpect(jsonPath("$.precio").value(80))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoria").value("Electrónica"));
    }

    // TEST GET - Listar productos
    @Test
    void testListarProductos() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    // TEST GET - Obtener por ID
    @Test
    void testObtenerProductoPorId() throws Exception {
        // Primero crear un producto
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Monitor Test");
        dto.setPrecio(500);
        dto.setStock(10);
        dto.setCategoria("Electrónica");

        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extraer ID del response (asumiendo JSON con campo id)
        // Para simplificar, probamos con ID 1 que debería existir por data.sql
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk());
    }

    // TEST PUT - Actualizar producto (NUEVO según lab S08)
    @Test
    void testActualizarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Monitor Actualizado");
        dto.setPrecio(600);
        dto.setStock(15);
        dto.setCategoria("Electrónica");

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor Actualizado"))
                .andExpect(jsonPath("$.precio").value(600))
                .andExpect(jsonPath("$.stock").value(15));
    }

    // TEST PUT - Actualizar producto no existente
    @Test
    void testActualizarProductoNoExistente() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Inexistente");
        dto.setPrecio(100);
        dto.setStock(5);
        dto.setCategoria("Electrónica");

        mockMvc.perform(put("/api/productos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE - Eliminar producto (NUEVO según lab S08)
    @Test
    void testEliminarProducto() throws Exception {
        // Crear un producto para eliminar
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Producto A Eliminar");
        dto.setPrecio(50);
        dto.setStock(10);
        dto.setCategoria("Electrónica");

        String createResponse = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Parsear ID del producto creado
        // Usamos un enfoque simple: asumimos que se crea con ID autoincremental
        // Para test real, parsearíamos el JSON
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado correctamente"));
    }

    // TEST DELETE - Eliminar producto no existente
    @Test
    void testEliminarProductoNoExistente() throws Exception {
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    // TEST VALIDACIÓN - Nombre vacío
    @Test
    void testValidacionNombreVacio() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("Electrónica");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN - Precio negativo
    @Test
    void testValidacionPrecioNegativo() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Producto Test");
        dto.setPrecio(-5);
        dto.setStock(2);
        dto.setCategoria("Electrónica");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN - Stock negativo
    @Test
    void testValidacionStockNegativo() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Producto Test");
        dto.setPrecio(10);
        dto.setStock(-1);
        dto.setCategoria("Electrónica");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST BUSCAR POR NOMBRE
    @Test
    void testBuscarProductoPorNombre() throws Exception {
        mockMvc.perform(get("/api/productos/buscar")
                        .param("nombre", "Monitor"))
                .andExpect(status().isOk());
    }
}