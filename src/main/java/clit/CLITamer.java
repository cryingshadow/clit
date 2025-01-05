package clit;

import java.lang.reflect.*;
import java.util.*;

public class CLITamer<T extends Enum<T> & Parameter> {

    private final T[] parameters;

    @SuppressWarnings("unchecked")
    public CLITamer(final Class<T> parameterClass)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Method values = parameterClass.getMethod("values");
        this.parameters = (T[])values.invoke(parameterClass);
    }

    public String getParameterDescriptions() {
        final StringBuilder builder = new StringBuilder();
        for (final T parameter : this.parameters) {
            builder.append("-");
            builder.append(parameter.shortName());
            builder.append(", --");
            builder.append(parameter.longName());
            builder.append(":\n");
            builder.append(parameter.description());
            builder.append("\n");
        }
        return builder.toString();
    }

    public Parameters<T> parse(final String[] args) {
        final Parameters<T> result = new Parameters<T>();
        for (int i = 0; i < args.length; i++) {
            final String currentArg = args[i];
            final Optional<String> nextArg = i == args.length - 1 ? Optional.empty() : Optional.of(args[i + 1]);
            final Optional<T> potentialParameter = this.parseParameter(currentArg);
            if (potentialParameter.isEmpty()) {
                throw new IllegalArgumentException("Arguments do not match available flags!");
            }
            final T parameter = potentialParameter.get();
            if (nextArg.isEmpty() || this.parseParameter(nextArg.get()).isPresent()) {
                result.put(parameter, "true");
            } else {
                result.put(parameter, nextArg.get());
                i++;
            }
        }
        return result;
    }

    private Optional<T> parseParameter(final String arg) {
        for (final T parameter : this.parameters) {
            if (
                arg.equalsIgnoreCase("-" + parameter.shortName())
                || arg.equalsIgnoreCase("--" + parameter.longName())
            ) {
                return Optional.of(parameter);
            }
        }
        return Optional.empty();
    }

}
