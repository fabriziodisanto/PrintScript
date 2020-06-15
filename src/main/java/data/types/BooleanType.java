package data.types;

import data.values.DataTypeValue;

public class BooleanType implements Type {

    private static final BooleanType BOOLEAN_TYPE = new BooleanType();

    private BooleanType() {}

    public static BooleanType getInstance() {
        return BOOLEAN_TYPE;
    }

    @Override
    public Boolean readAsBoolean(DataTypeValue dataTypeValue) {
        //        todo mhmh
        return (Boolean) dataTypeValue.getValue();
    }

    @Override
    public Double readAsNumber(DataTypeValue dataTypeValue) {
        //        todo error
        return null;
    }

    @Override
    public String readAsString(DataTypeValue dataTypeValue) {
        //        todo mhmh
        return dataTypeValue.getValue().toString();
    }
}
