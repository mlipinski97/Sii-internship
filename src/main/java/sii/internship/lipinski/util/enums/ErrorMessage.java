package sii.internship.lipinski.util.enums;

public enum ErrorMessage {

    LOGIN_ALREADY_TAKEN("Podany login jest juz zajety");

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
