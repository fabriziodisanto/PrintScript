package data.types;

import data.values.DataTypeValue;

public class StringType implements Type {

    private static final StringType STRING_TYPE = new StringType();

    private StringType() {}

    public static StringType getInstance() {
     return STRING_TYPE;
    }

    @Override
    public Boolean readAsBoolean(DataTypeValue dataTypeValue) {
        //        todo error
        return null;
    }

    @Override
    public Double readAsNumber(DataTypeValue dataTypeValue) {
        //        todo error
        return null;
    }

    @Override
    public String readAsString(DataTypeValue dataTypeValue) {
        //        todo mmhmh
        return dataTypeValue.getValue().toString();
    }
}
