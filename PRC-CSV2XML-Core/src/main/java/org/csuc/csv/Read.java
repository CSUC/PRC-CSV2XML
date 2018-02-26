package org.csuc.csv;

public interface Read<T> {

     <T> T readCSV();
     <T> T readCSVRelation();

}
