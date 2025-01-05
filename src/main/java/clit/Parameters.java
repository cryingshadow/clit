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

    public int getAsInt(final T parameter) {
        return Integer.parseInt(this.get(parameter));
    }

}
