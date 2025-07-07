package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = {"start_test","s"},
            value = "Start Application")
    public void runTest() {
        testRunnerService.run();
    }
}