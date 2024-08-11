import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestServiceImplTest {
    private TestServiceImpl testService;
    private QuestionDao questionDao;
    private IOService ioService;

    @BeforeEach
    void setUp() {
        questionDao = mock(QuestionDao.class);
        ioService = mock(IOService.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void correctLastQuestion() {
        Question lastQuestion = new Question("What is the periodic symbol for oxygen?",
                List.of(
                        new Answer("X", false),
                        new Answer("Y", false),
                        new Answer("O", true),
                        new Answer("G", false)));

        List<Question> listWithLastQuestion = List.of(lastQuestion);


        given(questionDao.findAll()).willReturn(listWithLastQuestion);

        testService.executeTest();

        verify(ioService).printFormattedLine("What is the periodic symbol for oxygen?");
        verify(ioService).printFormattedLine("X");
        verify(ioService).printFormattedLine("Y");
        verify(ioService).printFormattedLine("O");
        verify(ioService).printFormattedLine("G");
    }

}
