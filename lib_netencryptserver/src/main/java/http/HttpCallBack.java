package http;

/**
 * http请求通知，将http的请求消息传出，并接受业务返回
 */
public interface HttpCallBack {
    /**
     * 收到消息回调通知，
     *
     * @param request
     * @return
     */
    byte[] onResponse(String request);
}
