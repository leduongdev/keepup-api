package com.ra.base_spring_boot.exception;

public class UnAuthorizedException extends RuntimeException
{
    public UnAuthorizedException(String message)
    {
        super(message);
    }
}
