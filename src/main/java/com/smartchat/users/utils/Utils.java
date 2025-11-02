package com.smartchat.users.utils;

import java.util.Objects;

public class Utils {

    public static String getUserIdFromToken(String token){
        if(Objects.isNull(token)){
            throw new RuntimeException("Empty token, not possible to retrieve userId");
        }
        return token.substring(0, 26);
    }
}
