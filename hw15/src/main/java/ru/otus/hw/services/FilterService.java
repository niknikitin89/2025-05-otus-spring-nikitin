package ru.otus.hw.services;

import ru.otus.hw.models.CustomerWish;

public interface FilterService {

    Boolean readyToPay(CustomerWish wish);
}
