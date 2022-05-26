package sii.internship.lipinski.util.enums;

public enum ErrorCode {

    LOGIN_ALREADY_TAKEN(1001),
    LECTURE_NOT_FOUND(1002),
    NO_SEATS_AVAILABLE(1003);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    ErrorCode(Integer value) {
        this.value = value;
    }
}
