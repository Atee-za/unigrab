package com.unigrab.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException{
    private String code;
    public BadRequestException(String code, String e){
        super(e);
        this.code = code;
    }

}
