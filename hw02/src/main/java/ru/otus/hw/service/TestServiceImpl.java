package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printQuestion(question);
            var studentAnswer = ioService.readString();
            var isAnswerValid = checkCorrectAnswer(question.answers(), studentAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        for (var answer : question.answers()) {
            ioService.printFormattedLine(answer.text());
        }
    }

    private boolean checkCorrectAnswer(List<Answer> answerList, String studentAnswer) {
        for (var answer : answerList) {
            if (Objects.equals(studentAnswer, answer.text()) && answer.isCorrect()) {
                return true;
            }
        }
        return false;
    }
}
