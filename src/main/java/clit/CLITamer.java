package clit;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class CLITamer<T extends Enum<T> & Parameter> {

    private final T[] parameters;

    private final Set<T> required;

    public CLITamer(final Class<T> parameterClass)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this(parameterClass, Collections.emptySet());
    }

    @SuppressWarnings("unchecked")
    public CLITamer(final Class<T> parameterClass, final Set<T> required)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Method values = parameterClass.getMethod("values");
        this.parameters = (T[])values.invoke(parameterClass);
        this.required = required;
    }

    public String getParameterDescriptions() {
        final StringBuilder builder = new StringBuilder();
        for (final T parameter : this.parameters) {
            builder.append("-");
            builder.append(parameter.shortName());
            builder.append(", --");
            builder.append(parameter.longName());
            builder.append(":\n\n");
            if (this.required.contains(parameter)) {
                builder.append("Required.\n\n");
            }
            builder.append(parameter.description());
            builder.append("\n\n");
            final Set<? extends Parameter> dependencies = parameter.dependencies();
            if (!dependencies.isEmpty()) {
                builder.append("Dependencies:\n");
                builder.append(dependencies.stream().map(Parameter::shortName).collect(Collectors.joining(", ")));
                builder.append("\n\n");
            }
            final Set<? extends Parameter> incompatible = parameter.incompatible();
            if (!incompatible.isEmpty()) {
                builder.append("Incompatible:\n");
                builder.append(incompatible.stream().map(Parameter::shortName).collect(Collectors.joining(", ")));
                builder.append("\n\n");
            }
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
        this.validate(result);
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

    @SuppressWarnings("unlikely-arg-type")
    private void validate(final Parameters<T> result) {
        final Set<T> keys = result.keySet();
        if (!keys.containsAll(this.required)) {
            final Set<T> missing = new TreeSet<T>(this.required);
            missing.removeAll(keys);
            throw new IllegalArgumentException(
                String.format(
                    "Missing required parameters: %s",
                    missing.stream().map(Parameter::shortName).collect(Collectors.joining(", "))
                )
            );
        }
        for (final T key : keys) {
            if (!keys.containsAll(key.dependencies())) {
                final Set<Parameter> missing = new TreeSet<Parameter>(key.dependencies());
                missing.removeAll(keys);
                throw new IllegalArgumentException(
                    String.format(
                        "Missing dependencies for parameter %s: %s",
                        key.shortName(),
                        missing.stream().map(Parameter::shortName).collect(Collectors.joining(", "))
                    )
                );
            }
            final Set<Parameter> incompatible = new TreeSet<Parameter>(key.incompatible());
            incompatible.retainAll(keys);
            if (!incompatible.isEmpty()) {
                throw new IllegalArgumentException(
                    String.format(
                        "Incompatible parameters for parameter %s: %s",
                        key.shortName(),
                        incompatible.stream().map(Parameter::shortName).collect(Collectors.joining(", "))
                    )
                );
            }
        }
    }

}
