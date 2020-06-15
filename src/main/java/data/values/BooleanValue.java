package data.values;

import data.types.BooleanType;

public class BooleanValue<T extends Boolean> implements DataTypeValue {

    private Boolean value;
    private BooleanType type;


    public BooleanValue(Boolean value) {
        this.value = value;
        this.type = BooleanType.getInstance();
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public BooleanType getType() {
        return type;
    }
}
