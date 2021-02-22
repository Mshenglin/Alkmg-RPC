package com.xu.exception;

import java.io.Serializable;

/**
 * 序列化异常
 * @author
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String message){
        super(message);
    }
}
