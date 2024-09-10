package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TestServiceImplTest {
    private TestServiceImpl testService;
    private QuestionDao questionDao;
    private IOService ioService;
    private List<Question> listQuestion;

    @BeforeEach
    void setUp() {
        questionDao = mock(QuestionDao.class);
        ioService = mock(IOService.class);
        testService = new TestServiceImpl(ioService, questionDao);
        Question question = new Question("What is the periodic symbol for oxygen?",
                List.of(
                        new Answer("X", false),
                        new Answer("Y", false),
                        new Answer("O", true),
                        new Answer("G", false)));

        listQuestion = List.of(question);
    }

    @Test
    void correctQuestionAnswer() {
        var expected = 1;

        given(questionDao.findAll()).willReturn(listQuestion);
        given(ioService.readString()).willReturn("O");

        var student = new Student("Test", "Testov");

        var testResults = testService.executeTestFor(student);

        assertEquals(expected, testResults.getRightAnswersCount());
    }

    @Test
    void incorrectQuestionAnswer() {
        var expected = 0;

        given(questionDao.findAll()).willReturn(listQuestion);
        given(ioService.readString()).willReturn("X");

        var student = new Student("Test", "Testov");

        var testResults = testService.executeTestFor(student);

        assertEquals(expected, testResults.getRightAnswersCount());
    }

}
