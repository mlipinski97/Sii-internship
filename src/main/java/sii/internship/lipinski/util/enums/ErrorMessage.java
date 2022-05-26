package sii.internship.lipinski.util.enums;

public enum ErrorMessage {

    LOGIN_ALREADY_TAKEN("Podany login jest juz zajety"),
    LECTURE_NOT_FOUND("Wskazana prelekcja nie mogla zostac znaleziona"),
    NO_SEATS_AVAILABLE("Brak wolnych miejsc na ta prelekcje");

    private String message;

    public String getMessage()
    {
        return this.message;
    }

    ErrorMessage(String message)
    {
        this.message = message;
    }
}
