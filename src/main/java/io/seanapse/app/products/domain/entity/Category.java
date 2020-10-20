package io.seanapse.app.products.domain.entity;

public enum Category {
    BOOKS("Books"),
    MUSIC("Music"),
    MOVIES("Movies"),
    GAMES("Games");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
