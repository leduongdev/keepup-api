package com.ra.base_spring_boot.exception;

public class ForbiddenException extends RuntimeException
{
    public ForbiddenException(String message)
    {
        super(message);
    }
}
