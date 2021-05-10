package com.switchfully.spectangular.account;

import org.assertj.core.util.Lists;

import java.util.List;

public enum Role {

    Admin();


    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Lists.newArrayList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
