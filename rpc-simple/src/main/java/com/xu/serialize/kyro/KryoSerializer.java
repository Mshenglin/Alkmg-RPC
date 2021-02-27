package com.xu.serialize.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xu.exception.SerializeException;
import com.xu.remoting.dto.RpcRequest;
import com.xu.remoting.dto.RpcResponse;
import com.xu.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 使用kryo序列化
 * @author mashenglin
 */
public class KryoSerializer implements Serializer {
    /**
     *因为Kryo不是线程安全的。因此，使用ThreadLocal存储Kryo对象
     */

     private final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            // Configure the Kryo instance.
            kryo.register(RpcResponse.class);
            kryo.register(RpcRequest.class);
            return kryo;
        };
    };
    /**
     *
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    @Override
    public byte[] serialize(Object obj) {
        try(
            ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
            //创建输入流
            Output output=new Output(byteArrayOutputStream)){
                //从ThreadLocal中获取kryo对象
                Kryo kryo = kryos.get();
                //将传入的对象序列化为byte数组
            kryo.writeObject(output,obj);
            kryos.remove();
            return output.toBytes();
            } catch (Exception e){
            throw new SerializeException("Serialization failed");
        }
    }

    /**
     * 反序列化
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类
     * @param <T> 通配符注解
     * @return
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz){
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryos.get();
            // byte->Object: 从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryos.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}
