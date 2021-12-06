package controllers;


import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * create WebSocket
 *
 * inspired by https://github.com/playframework/play-java-websocket-example/blob/2.6.x/test/controllers/WebSocketClient.java
 */

public class WebSocketClient {
    private AsyncHttpClient client;

    /**
     * Constructor
     * @param c AsyncHttpClient
     */
    public WebSocketClient(AsyncHttpClient c) {
        this.client = c;
    }

    /**
     *
     * @param url
     * @param listener
     * @return CompletableFuture<WebSocket> future.toCompletableFuture
     * @throws ExecutionException exception
     * @throws InterruptedException exception
     */
    public CompletableFuture<NettyWebSocket> call(String url, WebSocketListener listener) throws ExecutionException, InterruptedException {
        final BoundRequestBuilder requestBuilder = client.prepareGet(url);

        final WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build();
        final ListenableFuture<NettyWebSocket> future = requestBuilder.execute(handler);
        return future.toCompletableFuture();
    }

    /**
     * LoggingListener
     */
    static class LoggingListener implements WebSocketListener {
        private final Consumer<String> onMessageCallback;
        private Throwable throwableFound = null;

        LoggingListener(Consumer<String> onMessageCallback) {
            this.onMessageCallback = onMessageCallback;
        }

        @Override
        public void onOpen(WebSocket webSocket) {

        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s) {

        }

        @Override
        public void onError(Throwable throwable) {
            this.throwableFound = throwable;

        }
        public void onMessage(String s) {
            onMessageCallback.accept(s);
        }
    }

}
