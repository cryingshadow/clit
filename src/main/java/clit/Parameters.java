package clit;

import java.util.*;

public class Parameters<T extends Enum<T> & Flag> extends LinkedHashMap<T, String> {

    private static final long serialVersionUID = 1L;

    public boolean getAsBoolean(final T flag) {
        return Boolean.parseBoolean(this.get(flag));
    }

    public int getAsInt(final T flag) {
        return Integer.parseInt(this.get(flag));
    }

}
