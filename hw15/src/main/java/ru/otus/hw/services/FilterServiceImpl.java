package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;

@Service
@Slf4j
public class FilterServiceImpl implements FilterService {

    @Override
    public Boolean readyToPay(CustomerWish wish) {
        log.info("======================>>>>> \"%s\" - ready to pay - %b"
                .formatted(wish.productName(), wish.readyToPay()));
        return wish.readyToPay();
    }
}
