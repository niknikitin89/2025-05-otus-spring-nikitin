package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll()
        throws QuestionReadException {

        try (InputStream resource = getFileAsStream();
             InputStreamReader reader = new InputStreamReader(resource)) {

            List<QuestionDto> questionsDto = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build()
                    .parse();

            if (questionsDto == null || questionsDto.isEmpty()) {
                throw new QuestionReadException("No question read");
            }

            return questionsDto.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("Error reading file", e);
        } catch (RuntimeException e) {
            throw new QuestionReadException("Error reading csv file", e);
        }
    }

    private InputStream getFileAsStream() throws IOException {
        String filename = fileNameProvider.getTestFileName();

        if (filename == null || filename.isEmpty()) {
            throw new IOException("Empty filename");
        }

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(fileNameProvider.getTestFileName());

        if (resource == null) {
            throw new IOException("No file with questions in application resource");
        }

        return resource;
    }
}
