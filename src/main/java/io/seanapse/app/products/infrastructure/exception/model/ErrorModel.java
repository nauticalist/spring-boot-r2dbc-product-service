package io.seanapse.app.products.infrastructure.exception.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import reactor.util.annotation.NonNull;

public class ErrorModel {
    @NonNull
    private int code;

    @NonNull
    private String message;

    @NonNull
    private String description;

    public ErrorModel() {
    }

    public ErrorModel(int code, @NonNull String message, @NonNull String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ErrorModel)) return false;

        ErrorModel that = (ErrorModel) o;

        return new EqualsBuilder()
                .append(getCode(), that.getCode())
                .append(getMessage(), that.getMessage())
                .append(getDescription(), that.getDescription())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCode())
                .append(getMessage())
                .append(getDescription())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
