package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private static final int SKIP_LINES_NUMBER = 1;

    private static final char SEPARATOR = ';';

    private static final String NO_FILE_NAME = "Empty filename";

    private static final String NO_FILE_IN_RESOURCE_FOLDER = "No file with questions in application resource";

    private static final String ERROR_FILE_READING = "Error reading file";

    private static final String NO_QUESTION_READ = "No question read";

    private static final String ERROR_CSV_READ = "Error reading csv file";

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        try (InputStream resource = getFileAsStream();
             InputStreamReader reader = new InputStreamReader(resource)) {

            List<QuestionDto> questionsDto = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withSkipLines(SKIP_LINES_NUMBER)
                    .withSeparator(SEPARATOR)
                    .withType(QuestionDto.class)
                    .build()
                    .parse();

            if (questionsDto == null || questionsDto.isEmpty()) {
                throw new QuestionReadException(NO_QUESTION_READ);
            }

            return questionsDto.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException(ERROR_FILE_READING, e);
        } catch (RuntimeException e) {
            throw new QuestionReadException(ERROR_CSV_READ, e);
        }
    }

    private InputStream getFileAsStream() throws IOException {
        String filename = fileNameProvider.getTestFileName();

        if (filename == null || filename.isEmpty()) {
            throw new IOException(NO_FILE_NAME);
        }

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(fileNameProvider.getTestFileName());

        if (resource == null) {
            throw new IOException(NO_FILE_IN_RESOURCE_FOLDER);
        }

        return resource;
    }
}
