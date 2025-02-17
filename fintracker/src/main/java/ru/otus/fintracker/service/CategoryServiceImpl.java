package ru.otus.fintracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.mapper.CategoryMapper;
import ru.otus.fintracker.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto findById(long id) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(id).orElse(null));
    }

    @Override
    public CategoryDto findByName(String name) {
        return categoryMapper.toCategoryDto(categoryRepository.findByName(name));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll());
    }

    @Override
    public CategoryDto update(CategoryDto updatedCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(updatedCategoryDto)));
    }

    @Override
    public CategoryDto create(CategoryDto newCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }
}
