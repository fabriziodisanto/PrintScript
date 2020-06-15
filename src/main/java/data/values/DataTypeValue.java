package data.values;

import data.types.Type;

public interface DataTypeValue<T> {
    T getValue();
    Type getType();
}
