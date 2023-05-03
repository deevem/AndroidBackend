package com.thss.androidbackend.model.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ResponseVo<T> {
    private final Integer code;
    private final String message;
    private final T data;
    public ResponseVo(T data){
        this.code = null;
        this.message = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }
}
