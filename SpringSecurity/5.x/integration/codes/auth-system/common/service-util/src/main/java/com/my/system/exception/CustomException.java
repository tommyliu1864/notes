package com.my.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{

    private Integer code;
    private String message;

}
