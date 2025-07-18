package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocaleConfig;

@RequiredArgsConstructor
@Service
@Primary
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {

        return messageSource.getMessage(code, args, localeConfig.getLocale());
    }
}
