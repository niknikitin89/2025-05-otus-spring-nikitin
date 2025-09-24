package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Process;
import ru.otus.hw.models.WorkStatus;

@Service
@Slf4j
public class DevelopmentServiceImpl implements DevelopmentService {

    @Override
    public Process develop(Process process) {
        //Разработка пройдет в синхронном режиме
        //Нужно только поменять статус, поэтому отдельный сервис не буду делать
        process.getDev().setStatus(WorkStatus.DONE);

        log.info("======================>>>>> \"%s\" - development completed"
                .formatted(process.getProductName()));
        return process;
    }
}
