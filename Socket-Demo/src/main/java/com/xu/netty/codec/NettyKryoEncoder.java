package com.xu.netty.codec;

/**
 * 自定义编码器。负责处理"出站"消息，将消息格式转换为字节数组然后写入到字节数据的容器 ByteBuf 对象中。
 *  * <p>
 *  * 网络传输需要通过字节流来实现，ByteBuf 可以看作是 Netty 提供的字节数据的容器，使用它会让我们更加方便地处理字节数据。
 * @author mashenglin
 */


import com.xu.netty.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private  final Class<?> genericClass;

    /**
     * 将对象转化为字节码写入ByteBuf对象中
     * @param channelHandlerContext
     * @param o 要编码的对象
     * @param byteBuf 写入的容器
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(o)){
            //将对象序列化为byte
            byte[] body = serializer.serialize(o);
            // 2. 读取消息的长度
            int dataLength = body.length;
            // 3.写入消息对应的字节数组长度,writerIndex 加 4
            byteBuf.writeInt(dataLength);
            //4.将字节数组写入 ByteBuf 对象中
            byteBuf.writeBytes(body);
        }
    }
}
