package data.types;

import data.values.DataTypeValue;

public class StringType implements Type {

    private static final StringType STRING_TYPE = new StringType();

    private StringType() {}

    public static StringType getInstance() {
     return STRING_TYPE;
    }
}
