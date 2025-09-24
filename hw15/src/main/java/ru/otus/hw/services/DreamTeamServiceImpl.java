package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.Development;
import ru.otus.hw.models.Process;
import ru.otus.hw.models.TestType;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.WorkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DreamTeamServiceImpl implements DreamTeamService {

    private static final String[] NAMES = {"Petya", "Kolya", "Vasya", "Masha", "Pasha"};

    @Override
    public Process createDevTeam(CustomerWish customerWish) {

        Random random = new Random();
        var randomNameNumber = random.nextInt(NAMES.length);

        //Выбираем разраба
        Development dev = new Development(NAMES[randomNameNumber], WorkStatus.WAITING);

        //Собираем команду тестировщиков
        List<Test> test = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            randomNameNumber = random.nextInt(NAMES.length);
            test.add(new Test(NAMES[randomNameNumber], TestType.values()[i], WorkStatus.WAITING));
        }

        var process = new Process(customerWish.productName(), dev, test);
        log.info("======================>>>>> \"%s\" - team created"
                .formatted(customerWish.productName()));
        log.info(process.toString());

        return process;

    }
}
