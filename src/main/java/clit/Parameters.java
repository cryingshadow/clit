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
    public boolean containsAtLeastOne(final T... flags) {
        for (final T flag : flags) {
            if (this.containsKey(flag)) {
                return true;
            }
        }
        return false;
    }

    public boolean getAsBoolean(final T flag) {
        return Boolean.parseBoolean(this.get(flag));
    }

    public int getAsInt(final T flag) {
        return Integer.parseInt(this.get(flag));
    }

}
