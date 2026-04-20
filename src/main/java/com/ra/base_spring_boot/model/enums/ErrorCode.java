package com.ra.base_spring_boot.model.enums;

public enum ErrorCode {
    // 9xxx: System
    UNCATEGORIZED_EXCEPTION("Uncategorized system error"),

    // 1xxx: Auth
    UNAUTHENTICATED("Authentication is required to access this resource"),
    UNAUTHORIZED("You do not have permission to access this resource"),
    INVALID_TOKEN("Invalid token"),
    TOKEN_ALREADY_USED("Token already used!"),
    TOKEN_EXPIRED("Token expired!"),
    CANNOT_SEND_EMAIL("Cannot send email!"),

    // 2xxx: Validation (Regex, NotBlank, etc.)
    INVALID_KEY("Invalid message key"),
    INVALID_PASSWORD_OR_EMAIL("Incorrect email or password"),

    // 3xxx: Business Logic (Duplicates, DB logic)
    USER_EXISTED("Username already exists"),
    EMAIL_EXISTED("Email is already in use"),
    USER_NOT_EXISTED("User does not exist"),

    // 4xxx: Resources
    DATA_NOT_FOUND("Requested data not found"),
    EMAIL_NOT_FOUND("Email not found"),
    USER_NOT_FOUND("User not found"),;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}