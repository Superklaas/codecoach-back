package com.switchfully.spectangular.domain;


import java.util.Arrays;
import java.util.List;

import static com.switchfully.spectangular.domain.Feature.*;

public enum Role {

    ADMIN(GET_USER_INFORMATION, CREATE_SESSION),
    COACH(GET_USER_INFORMATION, CREATE_SESSION),
    COACHEE(GET_USER_INFORMATION, BECOME_COACH, GET_ALL_COACHES, CREATE_SESSION);


    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Arrays.asList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
