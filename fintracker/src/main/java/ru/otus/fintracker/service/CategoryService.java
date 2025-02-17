package ru.otus.fintracker.service;

import java.util.List;
import ru.otus.fintracker.dto.CategoryDto;

public interface CategoryService {
    CategoryDto findById(long id);

    CategoryDto findByName(String name);

    List<CategoryDto> findAll();

    CategoryDto update(CategoryDto updatedCategory);

    CategoryDto create(CategoryDto newCategory);

    void delete(long id);
}
