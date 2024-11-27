package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        logger.info("New WebSocket connection established: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        for (WebSocketSession s : sessions) {
            try {
                s.sendMessage(message);
                logger.info("Message sent to session {}: {}", s.getId(), message.getPayload());
            } catch (IOException e) {
                logger.error("Failed to send message to session {}: {}", s.getId(), e.getMessage());
            }
        }
    }
}

