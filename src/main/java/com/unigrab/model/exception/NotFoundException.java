package com.unigrab.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException{
    private String code;

    public NotFoundException(String code, String e){
        super(e);
        this.code = code;
    }

}
