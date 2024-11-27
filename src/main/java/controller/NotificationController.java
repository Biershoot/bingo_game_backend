package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Notifica a los jugadores cuando se extrae una nueva balota.
     *
     * @param ball Número de la balota extraída.
     */
    public void notifyBallDrawn(int ball) {
        messagingTemplate.convertAndSend("/topic/balls", "Nueva balota: " + ball);
    }

    /**
     * Notifica a los jugadores cuando el juego termina.
     *
     * @param winner Nombre del ganador.
     */
    public void notifyGameEnded(String winner) {
        messagingTemplate.convertAndSend("/topic/game", "El juego ha terminado. Ganador: " + winner);
    }

    /**
     * Notifica a todos los jugadores cuando un jugador se une al lobby.
     *
     * @param playerName Nombre del jugador que se unió al lobby.
     */
    public void notifyPlayerJoined(String playerName) {
        messagingTemplate.convertAndSend("/topic/lobby", "Jugador " + playerName + " se ha unido al lobby.");
    }

    /**
     * Notifica que el juego ha comenzado.
     *
     * @param message Mensaje para los jugadores sobre el inicio del juego.
     */
    public void notifyGameStarted(String message) {
        messagingTemplate.convertAndSend("/topic/game", message);
    }
}


