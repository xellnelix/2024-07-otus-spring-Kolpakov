package ru.otus.fintracker.repository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.fintracker.BaseContainerTest;
import ru.otus.fintracker.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest extends BaseContainerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findCategoryByIdTest() {
        var foundCategory = categoryRepository.findById(1L);
        assertNotNull(foundCategory);
        assertEquals("TestCategoryFirst", foundCategory.orElseThrow(EntityNotFoundException::new).getName());
    }

    @Test
    void findAllCategoriesTest() {
        var foundCategories = categoryRepository.findAll();
        assertNotNull(foundCategories);
        assertEquals(foundCategories.size(), 3);
    }

    @Test
    void findCategoryByNameTest() {
        var expectedCategoryName = "TestCategorySecond";
        var foundCategory = categoryRepository.findByName(expectedCategoryName);
        assertNotNull(foundCategory);
        assertEquals(foundCategory.getName(), expectedCategoryName);
    }

    @Test
    void insertCategoryTest() {
        var categoryName = "HandmadeCategoryTest";
        var newCategory = new Category(20L, categoryName);
        categoryRepository.save(newCategory);
        var foundCategory = categoryRepository.findByName(categoryName);
        assertNotNull(foundCategory);
        assertEquals(foundCategory.getId(), 4L);
        assertEquals(foundCategory.getName(), categoryName);
    }

    @Test
    void updateCategoryTest() {
        var expectedCategory = new Category(1L, "HandmadeCategoryTest");
        var foundCategory = categoryRepository.findById(expectedCategory.getId());
        assertNotNull(foundCategory);
        assertNotEquals(Optional.of(expectedCategory), foundCategory);
        var updatedCategory = categoryRepository.save(expectedCategory);
        assertEquals(updatedCategory.getName(), "HandmadeCategoryTest");
        assertEquals(updatedCategory.getId(), 1L);
    }

    @Test
    void deleteCategoryTest() {
        var foundCategory = categoryRepository.findById(1L);
        assertNotNull(foundCategory);
        categoryRepository.deleteById(1L);
        assertThat(categoryRepository.findById(1L)).isEmpty();
    }
}
