package sii.internship.lipinski.util.enums;

public enum ErrorCode {

    LOGIN_ALREADY_TAKEN(1000);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    ErrorCode(Integer value) {
        this.value = value;
    }
}
