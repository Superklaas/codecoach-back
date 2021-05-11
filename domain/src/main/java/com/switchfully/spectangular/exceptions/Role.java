package com.switchfully.spectangular.exceptions;


import java.util.Arrays;
import java.util.List;

public enum Role {

    ADMIN(),
    COACHEE();


    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Arrays.asList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
