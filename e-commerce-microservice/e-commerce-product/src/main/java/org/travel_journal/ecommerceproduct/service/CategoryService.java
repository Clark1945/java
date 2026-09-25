package org.travel_journal.ecommerceproduct.service;

import org.springframework.stereotype.Service;
import org.travel_journal.ecommerceproduct.entity.Category;
import org.travel_journal.ecommerceproduct.dto.CategoryDTO;
import org.travel_journal.ecommerceproduct.repository.CategoryRepository;
import org.travel_journal.ecommerceproduct.utility.CategoryMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CategoryDTO createCategory(CategoryDTO dto) {
        Category entity = mapper.toEntity(dto);
        Category saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<CategoryDTO> getAllCategories() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("分類不存在"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setIconUrl(dto.getIconUrl());
        existing.setEnabled(dto.getEnabled());
        Category updated = repository.save(existing);
        return mapper.toDTO(updated);
    }
}
