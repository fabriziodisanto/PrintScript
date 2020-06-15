package data.types;

import data.values.DataTypeValue;

public interface Type {
    Boolean readAsBoolean(DataTypeValue dataTypeValue);
    Double readAsNumber(DataTypeValue dataTypeValue);
    String readAsString(DataTypeValue dataTypeValue);
}
