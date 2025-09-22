package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;

@Service
public class FilterServiceImpl implements FilterService {

    @Override
    public Boolean readyToPay(CustomerWish wish) {
        System.out.println("======================>>>>> FilterServiceImpl readyToPay");
        return wish.readyToPay();
    }
}
