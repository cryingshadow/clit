package clit;

import java.lang.reflect.*;
import java.util.*;

public class CLITamer<T extends Enum<T> & Parameter> {

    private final T[] flags;

    @SuppressWarnings("unchecked")
    public CLITamer(final Class<T> flagClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Method values = flagClass.getMethod("values");
        this.flags = (T[])values.invoke(flagClass);
    }

    public Parameters<T> parse(final String[] args) {
        final Parameters<T> result = new Parameters<T>();
        for (int i = 0; i < args.length; i++) {
            final String currentArg = args[i];
            final Optional<String> nextArg = i == args.length - 1 ? Optional.empty() : Optional.of(args[i + 1]);
            final Optional<T> potentialFlag = this.parseFlag(currentArg);
            if (potentialFlag.isEmpty()) {
                throw new IllegalArgumentException("Arguments do not match available flags!");
            }
            final T flag = potentialFlag.get();
            if (nextArg.isEmpty() || this.parseFlag(nextArg.get()).isPresent()) {
                result.put(flag, "true");
            } else {
                result.put(flag, nextArg.get());
                i++;
            }
        }
        return result;
    }

    private Optional<T> parseFlag(final String arg) {
        for (final T flag : this.flags) {
            if (
                arg.equalsIgnoreCase("-" + flag.shortName())
                || arg.equalsIgnoreCase("--" + flag.longName())
            ) {
                return Optional.of(flag);
            }
        }
        return Optional.empty();
    }

}
