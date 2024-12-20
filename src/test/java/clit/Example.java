package clit;

public enum Example implements Parameter {

    OTHER("o", "other", "This is another test description."),

    TEST("t", "test", "This is a test description.");

    private final String description;

    private final String longName;

    private final String shortName;

    private Example(final String shortName, final String longName, final String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String longName() {
        return this.longName;
    }

    @Override
    public String shortName() {
        return this.shortName;
    }

}
