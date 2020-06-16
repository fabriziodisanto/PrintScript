package data.types;

public class NumberType implements Type {

    private static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {}

    public static NumberType getInstance() {
        return NUMBER_TYPE;
    }
}
