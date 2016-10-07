package com.tradevalidator.model;

/**
 * Holder for one validation error
 */
public class ValidationError {

    private String field;
    private String message;

    public ValidationError() {}

    public String field() {
        return field;
    }

    public ValidationError field(String field) {
        this.field = field;
        return this;
    }

    public String message() {
        return message;
    }

    public ValidationError message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationError that = (ValidationError) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
