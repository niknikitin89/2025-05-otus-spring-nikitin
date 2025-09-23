package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.WorkStatus;

@Service
public class DevProcessHandlerImpl implements DevProcessHandler {

    @Override
    public DevelopmentProcess develop(DevelopmentProcess process) {
        //Разработка пройдет в синхронном режиме
        //Нужно только поменять статус, поэтому отдельный сервис не буду делать
        process.getDev().setStatus(WorkStatus.DONE);

        System.out.println("======================>>>>> DevProcessHandlerImpl develop");
        System.out.println("Dev DONE");
        return process;
    }
}
