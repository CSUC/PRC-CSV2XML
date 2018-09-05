package org.csuc.typesafe.altres;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Altres {

    public static boolean isValidCode(String code){
        Config conf = ConfigFactory.load("altres.json");

        return conf.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), code))
                .map(Map.Entry::getKey).findFirst().isPresent();
    }
}
