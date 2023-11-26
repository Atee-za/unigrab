package com.unigrab.util;

import com.unigrab.model.exception.*;

public class HandleExceptions {
    public static void throwBadRequestException(String code, String e){
        throw new BadRequestException(code, e);
    }
    public static void throwNotFoundException(String code, String e) { throw new NotFoundException(code, e); }
    public static void throwUnauthorizedException(String code, String e) { throw new UnAuthorizedException(code, e); }

}
