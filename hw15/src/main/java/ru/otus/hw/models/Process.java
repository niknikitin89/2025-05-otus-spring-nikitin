package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Process {

    private String productName;
    private Development dev;
    private List<Test> testList;
}
