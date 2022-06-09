package com.luslusdawmpfe.PFEBackent.exceptions;

public class EntityNotFoundException extends Exception{
    private String msg;

    public EntityNotFoundException(String msg){
        super(msg);
    }

}
