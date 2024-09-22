package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.service.TestRunnerService;

@Command
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @Command(description = "Run test", command = "start-test", alias = "st")
    public void startTest() {
        testRunnerService.run();
    }
}
