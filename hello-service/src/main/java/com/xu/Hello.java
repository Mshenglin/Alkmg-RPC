package com.xu;

import lombok.*;

/**
 * @author mashenglin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello {
    /**
     * 消息
     */
    private String message;
    /**
     * 描述
     */
    private String description;
}
