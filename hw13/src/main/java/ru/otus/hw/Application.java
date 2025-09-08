package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.printf("""
                Чтобы перейти на страницу сайта открывай: 
                http://localhost:8080 
                
                Аутентификация (логин пароль):
                admin admin - может делать все с книгами и комментариями
                user user - добавляет книги, правит и удаляет только свои. Комментарии может только добавлять
                
                """);
    }

}
