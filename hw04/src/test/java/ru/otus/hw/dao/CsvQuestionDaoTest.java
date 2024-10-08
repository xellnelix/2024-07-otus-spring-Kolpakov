package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CsvQuestionDao.class)
public class CsvQuestionDaoTest {
    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        testFileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    void correctReadFile() {
        var expectedQuestion = new Question("What is the periodic symbol for oxygen?", List.of(
                new Answer("X", false),
                new Answer("Y", false),
                new Answer("O", true),
                new Answer("G", false)));

        given(testFileNameProvider.getTestFileName()).willReturn("correct_question.csv");

        var questionList = csvQuestionDao.findAll();

        assertTrue(questionList.contains(expectedQuestion));
    }

    @Test
    void readEmptyFile() {

        given(testFileNameProvider.getTestFileName()).willReturn("nonexistent_questions.csv");

        var expected = assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
        String expectedMessage = "File reading error: ";

        assertEquals(expectedMessage, expected.getMessage());
    }
}
