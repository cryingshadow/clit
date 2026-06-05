package clit;

import java.util.*;

public interface Parameter {

    default Set<Parameter> dependencies() {
        return Collections.emptySet();
    }

    String description();

    default Set<Parameter> incompatible() {
        return Collections.emptySet();
    }

    String longName();

    String shortName();

}
