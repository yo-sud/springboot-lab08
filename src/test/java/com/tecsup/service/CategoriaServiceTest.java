package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST LISTAR
    @Test
    void testListarCategorias() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Electrónica", "Dispositivos electrónicos"),
                new Categoria(2L, "Hogar", "Artículos para el hogar")
        );
        when(repo.findAll()).thenReturn(lista);

        List<Categoria> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Electrónica", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    // TEST GUARDAR
    @Test
    void testGuardarCategoria() {
        Categoria categoria = new Categoria(1L, "Oficina", "Artículos de oficina");
        when(repo.findByNombre("Oficina")).thenReturn(Optional.empty());
        when(repo.save(categoria)).thenReturn(categoria);

        Categoria resultado = service.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Oficina", resultado.getNombre());
        verify(repo).save(categoria);
    }

    // TEST GUARDAR - DUPLICADO
    @Test
    void testGuardarCategoriaDuplicada() {
        Categoria existente = new Categoria(1L, "Electrónica", "Dispositivos electrónicos");
        Categoria duplicada = new Categoria(null, "Electrónica", "Otra descripción");

        when(repo.findByNombre("Electrónica")).thenReturn(Optional.of(existente));

        assertThrows(com.tecsup.exception.DuplicateResourceException.class,
                () -> service.guardar(duplicada));
        verify(repo, never()).save(any());
    }

    // TEST OBTENER - EXISTE
    @Test
    void testObtenerCategoriaExistente() {
        Categoria categoria = new Categoria(1L, "Teclado", "Accesorios");
        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNombre());
        verify(repo).findById(1L);
    }

    // TEST OBTENER - NO EXISTE
    @Test
    void testObtenerCategoriaNoExistente() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = service.obtener(99L);

        assertNull(resultado);
        verify(repo).findById(99L);
    }

    // TEST ACTUALIZAR
    @Test
    void testActualizarCategoria() {
        Categoria existente = new Categoria(1L, "Electrónica", "Dispositivos electrónicos");
        Categoria actualizada = new Categoria(1L, "Tecnología", "Dispositivos tecnológicos");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.findByNombre("Tecnología")).thenReturn(Optional.empty());
        when(repo.save(any(Categoria.class))).thenReturn(actualizada);

        Categoria resultado = service.actualizar(1L, actualizada);

        assertNotNull(resultado);
        assertEquals("Tecnología", resultado.getNombre());
        verify(repo).findById(1L);
        verify(repo).save(any(Categoria.class));
    }

    // TEST ACTUALIZAR - DUPLICADO DE NOMBRE
    @Test
    void testActualizarCategoriaNombreDuplicado() {
        Categoria existente = new Categoria(1L, "Electrónica", "Dispositivos electrónicos");
        Categoria otra = new Categoria(2L, "Hogar", "Artículos para el hogar");
        Categoria actualizada = new Categoria(1L, "Hogar", "Nueva descripción");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.findByNombre("Hogar")).thenReturn(Optional.of(otra));

        assertThrows(com.tecsup.exception.DuplicateResourceException.class,
                () -> service.actualizar(1L, actualizada));
        verify(repo, never()).save(any());
    }

    // TEST ELIMINAR
    @Test
    void testEliminarCategoria() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}