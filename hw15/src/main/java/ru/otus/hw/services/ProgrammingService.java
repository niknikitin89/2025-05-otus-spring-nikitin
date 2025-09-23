package ru.otus.hw.services;

import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.Product;

public interface ProgrammingService {

    Product createProduct(DevelopmentProcess process);
}
