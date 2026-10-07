package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<Producto> listar();

    Producto guardar(ProductoDTO productoDTO);

    Producto obtener(Long id);

    Producto actualizar(Long id, ProductoDTO productoDTO);

    void eliminar(Long id);

    List<Producto> buscarPorNombre(String nombre);
}