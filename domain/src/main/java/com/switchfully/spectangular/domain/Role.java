package com.switchfully.spectangular.domain;


import java.util.Arrays;
import java.util.List;

import static com.switchfully.spectangular.domain.Feature.BECOME_COACH;
import static com.switchfully.spectangular.domain.Feature.GET_USER_INFORMATION;

public enum Role {

    ADMIN(GET_USER_INFORMATION),
    COACH(GET_USER_INFORMATION),
    COACHEE(GET_USER_INFORMATION, BECOME_COACH);


    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Arrays.asList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
