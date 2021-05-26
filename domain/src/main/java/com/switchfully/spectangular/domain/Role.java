package com.switchfully.spectangular.domain;


import java.util.Arrays;
import java.util.List;

import static com.switchfully.spectangular.domain.Feature.*;

public enum Role {
    COACHEE(GET_USER_INFORMATION,  GET_ALL_COACHES, CREATE_SESSION, GET_ALL_COACHEE_SESSIONS, UPDATE_PROFILE,
            BECOME_COACH),
    COACH(GET_USER_INFORMATION,  GET_ALL_COACHES, CREATE_SESSION, GET_ALL_COACHEE_SESSIONS,  UPDATE_PROFILE,
            GET_ALL_COACHING_SESSION),
    ADMIN(GET_USER_INFORMATION,  GET_ALL_COACHES, CREATE_SESSION, GET_ALL_COACHEE_SESSIONS, UPDATE_PROFILE,
            GET_ALL_COACHING_SESSION);
    
    private final List<Feature> featureList;

    Role(Feature... featureList) {
        this.featureList = Arrays.asList(featureList);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }
}
