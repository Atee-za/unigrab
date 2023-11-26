package com.unigrab.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnAuthorizedException extends RuntimeException {
    private String code;

    public UnAuthorizedException(String code, String e){
        super(e);
        this.code = code;
    }

}
