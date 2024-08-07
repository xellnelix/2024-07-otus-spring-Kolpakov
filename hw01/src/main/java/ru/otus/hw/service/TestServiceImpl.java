package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questionList = questionDao.findAll();
        printQuestions(questionList);
        // Получить вопросы из дао и вывести их с вариантами ответов
    }

    private void printQuestions(List<Question> questionList) {
        for (Question question : questionList) {
            ioService.printFormattedLine(question.text());
            for (Answer answer : question.answers()) {
                ioService.printFormattedLine(answer.text());
            }
            ioService.printLine("\n");
        }
    }
}
