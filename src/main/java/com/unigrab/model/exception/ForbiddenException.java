package com.unigrab.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForbiddenException extends RuntimeException{
    private String code;
    public ForbiddenException(String code, String e){
        super(e);
        this.code = code;
    }

}
