package org.example.lostandfound.exception;

import org.example.lostandfound.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handleException(ServiceException e){
        e.printStackTrace();
        return Result.error(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage(), "400");
    }
}
