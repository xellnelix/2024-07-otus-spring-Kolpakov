package ru.otus.fintracker.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.fintracker.dto.CategoryDto;
import ru.otus.fintracker.model.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);

    Category toCategory(CategoryDto categoryDto);

    List<Category> toCategoryList(List<CategoryDto> categoryDtoList);
}
