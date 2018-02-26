package org.csuc.marshal;

/**
 * @author amartinez
 */
public class FactoryCERIF {

    public static <T> T createFactory(Factory factory) {
        factory.execute();
        return (T) factory;
    }


}
