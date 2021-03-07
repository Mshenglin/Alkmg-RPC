package com.xu.Socket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 发送消息实体类
 * @author
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
    private String content;
}
