import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class ServeCreatARMain {
    private static final int MAX_MESSAGE_SIZE = 3000000;
    private static final int PORT_NO = 9999;

    public static void main(String[] args) throws Exception {
        startServer(); //do need to see what happens when multiple devices access at the same time

    }

    private static void startServer() throws Exception {
        Server server = new Server(PORT_NO);
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