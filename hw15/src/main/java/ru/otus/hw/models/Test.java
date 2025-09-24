package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Test {

    private String testerName;
    private TestType type;
    private WorkStatus status;

}
