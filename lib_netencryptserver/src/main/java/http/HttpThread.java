package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpThread implements Runnable {
    private Socket socket;
    // 回调监听器，有业务方传入，网络不做业务处理
    private HttpCallBack callback;


    public HttpThread(Socket socket, HttpCallBack callback) {
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run() {
        // 1-读取客户端请求
        // 2-根据业务数据采取相应操作
        // 3-返回数据
        // 使用bufferReader可以提升I/O效率、便于逐行读入

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String content;
            StringBuilder request = new StringBuilder();
            //完成客户端请求内容逐行读入
            while ((content = reader.readLine()) != null && !content.trim().isEmpty()) {
                request.append(content).append("\n");
            }
            System.out.println("requst:\n" + request);
            //做一个返回数据
            byte[] response = new byte[0];
            if (callback != null) {
                response = callback.onResponse(request.toString());
            }

            // 将业务方数据，包装成http格式
            // 1-拼接请求行
            String responseFirstLine = "HTTP/1.1 200 OK \r\n";
            // 2-拼接请求头
            String responseHeader = "Content-type:" + "text/html" + "\r\n";
            // 3-空行
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(responseFirstLine.getBytes());
            outputStream.write(responseHeader.getBytes());
            outputStream.write("\r\n".getBytes());

            // 4-body
            outputStream.write(response);
            // 在socket close后。里面的outputStream和inputStream也会close
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
