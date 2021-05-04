package ac.echo;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class ProgressHandler {
    private WebSocketClient socket;

    private boolean started = false;

    ProgressHandler(String pin, ProgressListener progressListener) {

        try {
            socket = new WebSocketClient(new URI("wss://scanner.echo.ac/"), new Draft_6455()) {

                @Override
                public void onMessage(String message) {

                    if (message.split("\\$")[0].contains("NEW_PROGRESS")) {
                        String arg = message.split("\\$")[1];
                        
                        if (arg.equals("STARTED")){
                            if(!started){
                                progressListener.onScanStart();
                                started = true;
                            }
                        }
                        else if (arg.equals("FINISHED")){
                            progressListener.onScanFinish();
                            socket.close();
                        }
                        else {
                            try {
                                int progress = Integer.parseInt(arg);
                                progressListener.onProgressChange(progress);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());

                            }
                        }

                    }

                    if (message.contains("hello")) {
                        socket.send(pin + "|no|progress");
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    progressListener.onBeginListening();
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {}

                @Override
                public void onError(Exception ex) {
                    System.err.println(ex.getMessage());

                }
            };

            socket.connect();

        } catch (URISyntaxException e) {

        }
    }
}
