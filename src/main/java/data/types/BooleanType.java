package data.types;

import data.values.DataTypeValue;

public class BooleanType implements Type {

    private static final BooleanType BOOLEAN_TYPE = new BooleanType();

    private BooleanType() {}

    public static BooleanType getInstance() {
        return BOOLEAN_TYPE;
    }
}
