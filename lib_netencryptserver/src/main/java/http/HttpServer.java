package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private boolean mRunning;
    // 回调监听器，有业务方传入，网络不做业务处理
    private HttpCallBack callback;

    public HttpServer(HttpCallBack callback) {
        this.callback = callback;
    }

    // 编写接口函数，用于启动服务
    public void startHttpServer() {
        if (mRunning) {
            //如果在运行中，可以返回错误信息
            return;
        } else {
            mRunning = true;
            //启动socket
            try {
                ServerSocket serverSocket = new ServerSocket(80);
                //监听
                while (mRunning) {
                    //等待链接
                    Socket socket = serverSocket.accept();
                    System.out.printf("accept\r\n");
                    ExecutorService threadPool = Executors.newCachedThreadPool();
                    threadPool.execute(new HttpThread(socket, callback));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前请求的header对象，header为一个map
     *
     * @param request 请求数据
     * @return header的map形式
     */
    public static Map<String, String> getHeader(String request) {
        Map<String, String> header = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new StringReader(request));
            //逐行读取，request内容，读取到map中
            String line = reader.readLine();
            while (line != null && !line.trim().isEmpty()) {
                int p = line.indexOf(":");
                if (p >= 0) {
                    header.put(line.substring(0, p).trim().toLowerCase()
                            , line.substring(p + 1).trim());
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }
}
