package data;

public interface Type {
    Boolean readAsBoolean(DataTypeValue dataTypeValue);
    Double readAsNumber(DataTypeValue dataTypeValue);
    String readAsString(DataTypeValue dataTypeValue);
}
