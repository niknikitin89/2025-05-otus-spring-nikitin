package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.DevelopmentProcess;
import ru.otus.hw.models.Product;

@Service
public class ProgrammingServiceImpl implements ProgrammingService {

    @Override
    public Product createProduct(DevelopmentProcess process) {

        System.out.println("======================>>>>> ProgrammingServiceImpl createProduct");
        return new Product(process.getProductName());
    }
}
