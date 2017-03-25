import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.File;
import java.io.IOException;

@WebSocket
public class MyWebSocketHandler  {
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
    public void onMessage(byte[] data, int offset, int length) throws IOException {
        System.out.println("Wriiten");
        FileUtils.writeByteArrayToFile(new File("images/"+System.nanoTime()+"hello.jpg"), data);
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