package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.CustomerWish;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoftwareCreationServiceImpl implements SoftwareCreationService {

    private final ItCompany itCompany;

    @Override
    public void start() {

        List<CustomerWish> wishes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            wishes.add(createCustomerWish(i));
        }

        var productList = itCompany.create(wishes);
        if (productList != null) {
            log.info("======================>>>>> Products:");
            log.info("\n" +
                    productList.stream()
                            .map(p -> "%s - DEV %s, TEST %s"
                                    .formatted(p.name(), p.developStatus(), p.testStatus()))
                            .collect(Collectors.joining(", \n")));
        }
    }

    private CustomerWish createCustomerWish(int i) {

        Boolean readyToPay = Math.random() > 0.5 ? true : false;

        return new CustomerWish("Product %d".formatted(i), readyToPay);
    }
}