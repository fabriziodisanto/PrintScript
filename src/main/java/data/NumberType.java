package data;

public class NumberType implements Type {

    private static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {}

    public static NumberType getInstance() {
        return NUMBER_TYPE;
    }

    @Override
    public Boolean readAsBoolean(DataTypeValue dataTypeValue) {
        //        todo error
        return null;
    }

    @Override
    public Double readAsNumber(DataTypeValue dataTypeValue) {
        //        todo mhmh
        return (Double) dataTypeValue.getValue();
    }

    @Override
    public String readAsString(DataTypeValue dataTypeValue) {
        //        todo mhmh
        return dataTypeValue.getValue().toString();
    }
}
