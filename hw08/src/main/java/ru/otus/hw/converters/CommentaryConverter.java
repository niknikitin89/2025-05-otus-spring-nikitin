package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Commentary;

@Component
public class CommentaryConverter {
    public String commentaryToString(Commentary commentary) {
        return "- Id: %s, text: %s"
                .formatted(commentary.getId(), commentary.getText());
    }

}
