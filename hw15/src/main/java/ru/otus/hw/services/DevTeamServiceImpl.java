package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.Development;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.TestType;
import ru.otus.hw.models.Testing;
import ru.otus.hw.models.WorkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DevTeamServiceImpl implements DevTeamService {

    private static final String[] NAMES = {"Petya", "Kolya", "Vasya", "Masha", "Pasha"};

    @Override
    public DevelopmentProcess createDevTeam(CustomerWish customerWish) {

        Random random = new Random();
        var randomNameNumber = random.nextInt(NAMES.length);

        //Выбираем разраба
        Development dev = new Development(NAMES[randomNameNumber], WorkStatus.WAITING);

        //Собираем команду тестировщиков
        List<Testing> test = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            randomNameNumber = random.nextInt(NAMES.length);
            test.add(new Testing(NAMES[randomNameNumber], TestType.values()[i], WorkStatus.WAITING));
        }

        var process = new DevelopmentProcess(customerWish.productName(), dev, test);
        System.out.println("======================>>>>> DevTeamServiceImpl createDevTeam");
        System.out.println(process.toString());

        return process;

    }
}
