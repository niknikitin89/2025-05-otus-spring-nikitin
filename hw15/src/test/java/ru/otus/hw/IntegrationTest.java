package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.test.context.SpringIntegrationTest;
import ru.otus.hw.config.AppRunner;
import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.TestType;
import ru.otus.hw.models.WorkStatus;
import ru.otus.hw.services.ItCompany;
import ru.otus.hw.services.TestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@SpringIntegrationTest
class IntegrationTest {

    @MockBean
    private AppRunner runner;

    @MockBean
    private TestService testService;

    @Autowired
    private ItCompany company;

    private static final String PRODUCT_NAME1 = "Product1";

    private static final String PRODUCT_NAME2 = "Product2";

    private ru.otus.hw.models.Test compliteTest =
            new ru.otus.hw.models.Test("Tester", TestType.UNIT, WorkStatus.DONE);

    private ru.otus.hw.models.Test failedTest =
            new ru.otus.hw.models.Test("Tester", TestType.UNIT, WorkStatus.FAILED);

    @Test
    void createdOneProductOutOfTwo() {

        List<CustomerWish> wishes = List.of(
                new CustomerWish(PRODUCT_NAME1, true),
                new CustomerWish(PRODUCT_NAME2, false));

        when(testService.test(any())).thenReturn(compliteTest);

        var productList = company.create(wishes);

        assertThat(productList).isNotNull().hasSize(1);
        assertThat(productList.get(0).name()).isEqualTo(PRODUCT_NAME1);
        assertThat(productList.get(0).testStatus()).isEqualTo(WorkStatus.DONE);
    }

    @Test
    void noProductsCreated() {

        List<CustomerWish> wishes = List.of(
                new CustomerWish(PRODUCT_NAME1, false),
                new CustomerWish(PRODUCT_NAME2, false));

        when(testService.test(any())).thenReturn(compliteTest);

        var productList = company.create(wishes);

        assertThat(productList).isNull();
    }

    @Test
    void getProductWithFailedTest() {

        List<CustomerWish> wishes = List.of(
                new CustomerWish(PRODUCT_NAME1, true));

        when(testService.test(any())).thenReturn(failedTest);

        var productList = company.create(wishes);

        assertThat(productList).isNotNull().hasSize(1);
        assertThat(productList.get(0).name()).isEqualTo(PRODUCT_NAME1);
        assertThat(productList.get(0).testStatus()).isEqualTo(WorkStatus.FAILED);
    }

}
