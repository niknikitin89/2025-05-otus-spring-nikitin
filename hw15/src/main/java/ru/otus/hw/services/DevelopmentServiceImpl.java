package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DevelopmentServiceImpl implements DevelopmentService {

    private final ItCompany itCompany;

    @Override
    public void startProgramming() {

        List<CustomerWish> spec = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            spec.add(createCustomerWish(i));
        }

//        System.out.println(spec.toString());

        var product = itCompany.create(spec);
        if (product != null) {
            System.out.println(product.name());
        }


    }

    private CustomerWish createCustomerWish(int i) {

        Boolean readyToPay = Math.random() > 0.5 ? true : false;

        return new CustomerWish("Product %d".formatted(i), readyToPay);
    }
}
