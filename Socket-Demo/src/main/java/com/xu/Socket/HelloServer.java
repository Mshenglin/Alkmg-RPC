package com.xu.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 * @author Dell
 */
public class HelloServer {
    private static final Logger logger= LoggerFactory.getLogger(HelloServer.class);
    public void  start(int port){
        //1.创建一个ServerSocket对象并绑定一个端口号
        try(ServerSocket server=new ServerSocket(port);){
            Socket socket;
            //通过accept()方法监听客户端的请求
            while ((socket = server.accept())!=null) {
                logger.info("client connected");
                try(ObjectInputStream
                            objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            ){
                //3.通过输入获取发送的请求消息

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
