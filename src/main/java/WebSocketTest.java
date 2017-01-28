import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketTest{
    private static final int MAX_MESSAGE_SIZE = 3000000;

    public static void main(String[] args) throws Exception {
        Server server = new Server(9999);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.getPolicy().setMaxBinaryMessageSize(MAX_MESSAGE_SIZE);
                factory.register(MyWebSocketHandler.class);

            }
        };
        server.setHandler(wsHandler);
        server.start();
        server.join();
    }
}