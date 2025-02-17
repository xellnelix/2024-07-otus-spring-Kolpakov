package ru.otus.fintracker.mapper;

import java.util.List;
import java.util.stream.LongStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.model.Category;

@SpringBootTest
public class CategoryMapperTest extends BaseContainerTest {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void convertToCategoryDtoTest() {
        var category = getCategoryList().get(0);
        var categoryDto = categoryMapper.toCategoryDto(category);

        assertNotNull(categoryDto);
        assertEquals(category.getId(), categoryDto.id());
        assertEquals(category.getName(), categoryDto.name());
    }

    @Test
    void convertToCategoryTest() {
        var categoryDto = getCategoryDtoList().get(0);
        var category = categoryMapper.toCategory(categoryDto);

        assertNotNull(category);
        assertEquals(category.getId(), categoryDto.id());
        assertEquals(category.getName(), categoryDto.name());
    }

    @Test
    void convertToCategoryDtoList() {
        var CategoryList = getCategoryList();
        var CategoryDtoList = categoryMapper.toCategoryDtoList(CategoryList);

        assertNotNull(CategoryDtoList);
        assertEquals(CategoryDtoList.size(), CategoryList.size());
    }

    @Test
    void convertToCategoryList() {
        var CategoryDtoList = getCategoryDtoList();
        var CategoryList = categoryMapper.toCategoryList(CategoryDtoList);

        assertNotNull(CategoryList);
        assertEquals(CategoryList.size(), CategoryDtoList.size());
    }

    private static List<Category> getCategoryList() {
        return LongStream.range(1, 3).boxed()
                .map(id -> new Category(id, "Category_" + id))
                .toList();
    }

    private static List<CategoryDto> getCategoryDtoList() {
        return LongStream.range(1, 3).boxed()
                .map(id -> new CategoryDto(id, "Category_" + id))
                .toList();
    }
}
