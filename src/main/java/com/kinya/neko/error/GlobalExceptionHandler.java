package com.kinya.neko.error;


import com.kinya.neko.error.exception.NekoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        return new ResponseEntity(new ErroResponse(ex),HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.toString());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
