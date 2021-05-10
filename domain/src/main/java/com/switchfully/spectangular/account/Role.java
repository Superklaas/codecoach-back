package com.switchfully.spectangular.account;


import java.util.Arrays;
import java.util.List;

public enum Role {

    Admin();


    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Arrays.asList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
