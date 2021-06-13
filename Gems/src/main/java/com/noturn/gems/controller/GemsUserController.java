package com.noturn.gems.controller;

import java.util.HashMap;
import java.util.Map;

public class GemsUserController {

    private final Map<String, Double> userMap = new HashMap<>();

    public Double get(String userName) {
        return userMap.get(userName);
    }

    public void merge(String userName, double amount, boolean sum) {
        userMap.merge(userName, amount, (oldValue, newValue) -> {
            if (!sum) {
                double withdraw = oldValue - newValue;
                if (withdraw <= 0) {
                    return null;
                }

                return withdraw;
            }

            return oldValue + newValue;
        });
    }

    public Double put(String userName, double gems) {
        return userMap.put(userName, gems);
    }

    public Double remove(String userName) {
        return userMap.remove(userName);
    }

}
