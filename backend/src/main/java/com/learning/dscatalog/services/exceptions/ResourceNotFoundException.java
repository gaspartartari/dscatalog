package com.learning.dscatalog.services.exceptions;

public class ResourceNotFoundException extends RuntimeException  {

    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
