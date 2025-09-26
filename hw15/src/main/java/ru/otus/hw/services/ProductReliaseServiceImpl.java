package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Process;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Test;
import ru.otus.hw.models.WorkStatus;

import java.util.List;

@Service
@Slf4j
public class ProductReliaseServiceImpl implements ProductReliaseService {

    @Override
    public Product createProduct(Process process) {

        log.info("======================>>>>> \"%s\" - product created"
                .formatted(process.getProductName()));

        WorkStatus testStatus;
        var testList = process.getTestList();
        if (testList.size() == 3 && isTestsComplete(testList)) {
            testStatus = WorkStatus.DONE;
        } else {
            testStatus = WorkStatus.FAILED;
        }


        return new Product(process.getProductName(), WorkStatus.DONE, testStatus);
    }

    private static boolean isTestsComplete(List<Test> testList) {

        return testList.stream().allMatch(test -> test.getStatus() == WorkStatus.DONE);
    }
}
