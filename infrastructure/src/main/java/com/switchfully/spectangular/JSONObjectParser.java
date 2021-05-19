package com.switchfully.spectangular;

import org.json.JSONObject;

import java.util.Base64;

public class JSONObjectParser {

    public static JSONObject JwtTokenToJSONObject(String token){
        String splitToken = token.split(" ")[1];
        String[] chunks = splitToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
