package ru.otus.fintracker.service;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.mapper.CategoryMapper;
import ru.otus.fintracker.model.Category;
import ru.otus.fintracker.repository.CategoryRepository;

@SpringBootTest
public class CategoryServiceTest extends BaseContainerTest {
    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryMapper categoryMapper;

    @Test
    public void findCategoryByIdTest() {
        var expectedCategoryDto = new CategoryDto(1L, "TestCategoryFirst");
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category(1L, "TestCategoryFirst")));
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(expectedCategoryDto);
        assertEquals(categoryService.findById(1), expectedCategoryDto);
    }

    @Test
    public void findCategoryByNameTest() {
        var expectedCategoryDto = new CategoryDto(2L, "TestCategorySecond");
        when(categoryRepository.findByName(anyString())).thenReturn(new Category(2L, "TestCategorySecond"));
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(expectedCategoryDto);
        assertEquals(categoryService.findByName("TestCategorySecond"), expectedCategoryDto);
    }

    @Test
    public void findAllCategoriesTest() {
        var expectedCategoryDtoList = List.of(
                new CategoryDto(1L, "TestCategoryFirst"),
                new CategoryDto(2L, "TestCategorySecond"),
                new CategoryDto(3L, "TestCategoryThird"));
        var categories = categoryMapper.toCategoryList(expectedCategoryDtoList);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toCategoryDtoList(anyList())).thenReturn(expectedCategoryDtoList);
        assertEquals(categoryService.findAll(), expectedCategoryDtoList);
    }

    @Test
    public void updateCategoryTest() {
        var expectedCategoryDto = new CategoryDto(1L, "UpdatedCategory");
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(new Category(1L, "UpdatedCategory"));
        var mappedCategory = categoryMapper.toCategory(expectedCategoryDto);
        when(categoryRepository.save(any(Category.class))).thenReturn(mappedCategory);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(expectedCategoryDto);
        assertEquals(categoryService.update(expectedCategoryDto), expectedCategoryDto);
    }

    @Test
    public void createCategoryTest() {
        var expectedCategoryDto = new CategoryDto(4L, "NewCategory");
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(new Category(4L, "NewCategory"));
        var mappedCategory = categoryMapper.toCategory(expectedCategoryDto);
        when(categoryRepository.save(any(Category.class))).thenReturn(mappedCategory);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(expectedCategoryDto);
        assertEquals(categoryService.create(expectedCategoryDto), expectedCategoryDto);
    }

    @Test
    public void deleteCategoryTest() {
        categoryService.delete(1L);
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }
}
