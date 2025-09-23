package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.CustomerWish;

import java.util.List;

@MessagingGateway
public interface ItCompany {

    @Gateway(
            requestChannel = "wishChannel",
            replyChannel = "productChannel"
//            replyTimeout = 5000,    // 5 секунд на ответ
//            requestTimeout = 3000   // 3 секунды на отправку
    )
    Product create(List<CustomerWish> customerWish);
}
