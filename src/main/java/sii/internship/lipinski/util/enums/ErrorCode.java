package sii.internship.lipinski.util.enums;

public enum ErrorCode {

    LOGIN_ALREADY_TAKEN(1001),
    LECTURE_NOT_FOUND(1002),
    NO_SEATS_AVAILABLE(1003),
    LECTURE_SCHEDULES_COLLIDE(1004),
    USER_NOT_FOUND(1005),
    LECTURE_REGISTRATION_NOT_FOUND(1006);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    ErrorCode(Integer value) {
        this.value = value;
    }
}
