package data;

public class NumberValue<T extends Double> implements DataTypeValue {

    private Double value;
    private NumberType type;


    public NumberValue(Double value) {
        this.value = value;
        this.type = NumberType.getInstance();
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public NumberType getType() {
        return type;
    }
}
