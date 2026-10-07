package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST LISTAR
    @Test
    void testListarProductos() {
        List<Producto> lista = List.of(
                new Producto(1L, "Monitor", 500, 10, "Electrónica"),
                new Producto(2L, "Teclado", 100, 20, "Electrónica")
        );
        when(repo.findAll()).thenReturn(lista);

        List<Producto> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    // TEST GUARDAR
    @Test
    void testGuardarProducto() {
        Producto producto = new Producto(1L, "Laptop", 3000, 5, "Electrónica");
        when(repo.save(producto)).thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        verify(repo).save(producto);
    }

    // TEST OBTENER - EXISTE
    @Test
    void testObtenerProductoExistente() {
        Producto producto = new Producto(1L, "Teclado", 100, 20, "Electrónica");
        when(repo.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNombre());
        verify(repo).findById(1L);
    }

    // TEST OBTENER - NO EXISTE (NUEVO según lab S08)
    @Test
    void testObtenerProductoNoExistente() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.obtener(99L);

        assertNull(resultado);
        verify(repo).findById(99L);
    }

    // TEST ACTUALIZAR (NUEVO según lab S08)
    @Test
    void testActualizarProducto() {
        Producto existente = new Producto(1L, "Monitor", 500, 10, "Electrónica");
        Producto actualizado = new Producto(1L, "Monitor 27\"", 700, 5, "Electrónica");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Producto.class))).thenReturn(actualizado);

        Producto resultado = service.actualizar(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("Monitor 27\"", resultado.getNombre());
        assertEquals(700, resultado.getPrecio());
        verify(repo).findById(1L);
        verify(repo).save(any(Producto.class));
    }

    // TEST ELIMINAR
    @Test
    void testEliminarProducto() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}