package data.values;

import data.types.StringType;

public class StringValue<T extends String> implements DataTypeValue {

    private String value;
    private StringType type;


    public StringValue(String value) {
        this.value = value;
        this.type = StringType.getInstance();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public StringType getType() {
        return type;
    }
}
