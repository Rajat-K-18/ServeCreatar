import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class MyWebSocketHandler {
    private Session session;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            session.getRemote().sendString("Hello Webbrowser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(byte[] data, int offset, int length) {
        System.out.println(new String (data));
        try {
            this.session.getRemote().sendString("Receiving Binary Data==>"+data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("String Message ====> " + message);
        try {
            this.session.getRemote().sendString("Receiving String Data==>"+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}