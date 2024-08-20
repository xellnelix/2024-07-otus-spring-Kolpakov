import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CsvQuestionDaoTest {
    private TestFileNameProvider testFileNameProvider;

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

        var expected = assertThrows(QuestionReadException.class, () -> {
            csvQuestionDao.findAll();
        });
        String expectedMessage = "File is empty!";

        assertEquals(expectedMessage, expected.getMessage());
    }
}
