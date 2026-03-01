package com.ra.base_spring_boot.exception;

public class NotFoundException extends RuntimeException
{
    public NotFoundException(String message)
    {
        super(message);
    }
}
