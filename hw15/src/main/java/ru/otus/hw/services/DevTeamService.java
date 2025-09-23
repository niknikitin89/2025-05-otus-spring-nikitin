package ru.otus.hw.services;

import ru.otus.hw.models.CustomerWish;
import ru.otus.hw.models.DevelopmentProcess;

public interface DevTeamService {

    public DevelopmentProcess createDevTeam(CustomerWish customerWish);
}
