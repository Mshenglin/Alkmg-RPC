package com.xu;

import lombok.*;

import java.io.Serializable;

/**
 * @author mashenglin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    /**
     * 消息
     */
    private String message;
    /**
     * 描述
     */
    private String description;
}
