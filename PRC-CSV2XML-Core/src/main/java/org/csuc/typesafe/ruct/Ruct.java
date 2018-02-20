package org.csuc.typesafe.ruct;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Ruct {

    public static boolean isValidRuct(String code){
        Config conf = ConfigFactory.load("ruct.json");

        return conf.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), code))
                .map(Map.Entry::getKey).findFirst().isPresent();
    }
}
