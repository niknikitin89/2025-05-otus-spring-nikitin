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
    )
    List<Product> create(List<CustomerWish> customerWish);
}
