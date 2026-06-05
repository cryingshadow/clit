package clit;

import java.util.*;

public class Parameters<T extends Enum<T> & Parameter> extends LinkedHashMap<T, String> {

    private static final long serialVersionUID = 1L;

    public Parameters() {
        super();
    }

    public Parameters(final Map<T, String> map) {
        super(map);
    }

    @SuppressWarnings("unchecked")
    public boolean containsAtLeastOne(final T... parameters) {
        for (final T parameter : parameters) {
            if (this.containsKey(parameter)) {
                return true;
            }
        }
        return false;
    }

    public boolean getAsBoolean(final T parameter) {
        return Boolean.parseBoolean(this.get(parameter));
    }

    public boolean getAsBooleanOrDefault(final T parameter, final boolean defaultValue) {
        return this.containsKey(parameter) ? Boolean.parseBoolean(this.get(parameter)) : defaultValue;
    }

    public int getAsInt(final T parameter) {
        return Integer.parseInt(this.get(parameter));
    }

    public int getAsIntOrDefault(final T parameter, final int defaultValue) {
        return this.containsKey(parameter) ? Integer.parseInt(this.get(parameter)) : defaultValue;
    }

    public boolean hasKeySetToValue(final T key, final int value) {
        return this.containsKey(key) && Integer.parseInt(this.get(key)) == value;
    }

    public boolean hasKeySetToValue(final T key, final String value) {
        return this.containsKey(key) && this.get(key).equals(value);
    }

}
