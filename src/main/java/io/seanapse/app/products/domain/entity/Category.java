package io.seanapse.app.products.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Category {
    BOOKS("Books"),
    MUSIC("Music"),
    MOVIES("Movies"),
    GAMES("Games");

    @Getter
    private String value;

    Category(String value) {
        this.value = value;
    }
}
