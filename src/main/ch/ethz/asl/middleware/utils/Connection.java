package ch.ethz.asl.middleware.utils;

import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.IOException;
import java.net.*;

public class Connection{

    private ByteBuffer buffer;
    private SocketChannel socketChannel;
    public int Id;
    private boolean isBlocking;

    public Connection(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        // TODO: Check if 1kb is enough for all cases
        this.buffer = ByteBuffer.allocate(1024);
    }

    public void ConfigureBlocking(boolean isBlocking) throws IOException{
        socketChannel.configureBlocking(isBlocking);
        this.isBlocking = isBlocking;
    }

    public String read() throws IOException{
        if(this.isBlocking){
            return readBlocking();
        }
        return readNonBlocking();
    }

    private String readBlocking() throws IOException{
        buffer.clear();
        int totalBytesRead = 0;

        while(true){
            totalBytesRead += socketChannel.read(buffer);
            if(buffer.array()[totalBytesRead - 1] == 10){
                break;
            }
        }

        return new String(buffer.array()).substring(0, totalBytesRead);
    }

    private String readNonBlocking() throws IOException{
        buffer.clear();
        int totalBytesRead = socketChannel.read(buffer);

        if(totalBytesRead <= 0){
            return "";
        }

        return new String(buffer.array()).substring(0, totalBytesRead);
    }
}