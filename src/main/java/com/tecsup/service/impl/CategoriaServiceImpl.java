package com.tecsup.service.impl;

import com.tecsup.model.Categoria;
import com.tecsup.dto.CategoriaDTO;
import com.tecsup.repository.CategoriaRepository;
import com.tecsup.service.CategoriaService;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria guardar(CategoriaDTO categoriaDTO) {
        if (categoriaRepository.findByNombre(categoriaDTO.getNombre()).isPresent()) {
            throw new DuplicateResourceException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtener(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public Categoria actualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = obtener(id);

        if (!categoriaExistente.getNombre().equals(categoriaDTO.getNombre())) {
            if (categoriaRepository.findByNombre(categoriaDTO.getNombre()).isPresent()) {
                throw new DuplicateResourceException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
            }
        }

        categoriaExistente.setNombre(categoriaDTO.getNombre());
        categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());
        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}