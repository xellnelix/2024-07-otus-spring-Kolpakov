package ru.otus.fintracker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.service.CategoryService;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping("/category/new")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @GetMapping("/category/id/{id}")
    public CategoryDto getCategory(@PathVariable("id") long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/category/name/{name}")
    public CategoryDto getCategory(@PathVariable("name") String name) {
        return categoryService.findByName(name);
    }

    @PutMapping("/category/edited")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.delete(id);
    }
}
