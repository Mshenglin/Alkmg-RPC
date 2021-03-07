package com.xu.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户端
 * @author Dell
 */
public class HelloClient {
    private static final Logger logger= LoggerFactory.getLogger(HelloClient.class);
    public Object send(Message message,String host,int port){
        try(Socket socket=new Socket(host,port)){
            ObjectInputStream
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
            return objectInputStream.readObject();

        }catch (Exception e)
        {
            logger.error("occur expection",e);

        }
        return null;
    }

    public static void main(String[] args) {
         HelloClient helloClient = new HelloClient();
         Message message= (Message) helloClient.send(new Message("cintent from client"), "127.0.0.1", 6666);
        System.out.println("client trceive message"+message.getContent());
    }
}
