package com.jerry.testServer;

import com.jerry.api.AnimalService;

public class AnimalServiceImpl implements AnimalService {
    @Override
    public String sayName() {
        return "dog";
    }
}
