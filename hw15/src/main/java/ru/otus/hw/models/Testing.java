package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Testing {

    private String testerName;
    private TestType type;
    private WorkStatus status;

}
