package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.Product;

@Service
public class ProgrammingServiceImpl implements ProgrammingService {

    @Override
    public Product createProduct(CustomerWish wish) {

        System.out.println("======================>>>>> ProgrammingServiceImpl createProduct");
        return new Product(wish.productName());
    }
}
