package com.kinya.neko.error;


import com.kinya.neko.error.exception.NekoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @description global exception handler
 * @author white
 * @date 2022-01-14
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @author: white
     * @description: 业务逻辑中抛出异常统一处理
     * @date: 2022/1/14
     * @param ex 业务逻辑统一继承异常基类Class#
     * @param request 抛出异常时执行的请求
     * @return ResponseEntity
     **/
    @ExceptionHandler(NekoException.class)
    public ResponseEntity<ErroResponse> handlerAtRunTime(NekoException ex, WebRequest request){
        return new ResponseEntity(new ErroResponse(ex.getCode(), ex.getMsg()),HttpStatus.BAD_REQUEST);
    }
}
