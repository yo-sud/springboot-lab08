package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    List<Categoria> listar();

    Categoria guardar(CategoriaDTO categoriaDTO);

    Categoria obtener(Long id);

    Categoria actualizar(Long id, CategoriaDTO categoriaDTO);

    void eliminar(Long id);
}