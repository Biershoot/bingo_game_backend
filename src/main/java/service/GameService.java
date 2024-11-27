package service;

import controller.NotificationController;
import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GameRepository;

import java.time.LocalDateTime;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private NotificationController notificationController;

    /**
     * Inicia un nuevo juego y lo marca como activo.
     *
     * @return El juego recién creado.
     */
    public Game startGame() {
        Game game = Game.builder()
                .createdAt(LocalDateTime.now()) // Fecha de creación
                .active(true) // Inicializa el juego como activo
                .build();
        return gameRepository.save(game);
    }

    /**
     * Obtiene un juego por su ID, lanza excepción si no existe.
     *
     * @param gameId ID del juego.
     * @return El juego correspondiente.
     */
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("El juego con ID " + gameId + " no existe."));
    }

    /**
     * Finaliza un juego al marcarlo como inactivo.
     *
     * @param game Juego a finalizar.
     */
    public void endGame(Game game) {
        if (!game.isActive()) {
            throw new IllegalStateException("El juego ya está finalizado.");
        }
        game.setActive(false); // Marca el juego como inactivo
        gameRepository.save(game); // Guarda los cambios
    }

    /**
     * Declara al ganador de un juego.
     *
     * @param game   Juego actual.
     * @param player Jugador que ganó el juego.
     */
    public void declareWinner(Game game, Player player) {
        if (!game.isActive()) {
            throw new IllegalStateException("El juego ya está finalizado, no se puede declarar un ganador.");
        }
        game.setWinner(player.getUser()); // Asigna el ganador
        game.setActive(false); // Finaliza el juego
        gameRepository.save(game); // Guarda el estado actualizado
    }

    /**
     * Añade un jugador a un juego.
     *
     * @param game   El juego al que se añadirá el jugador.
     * @param player El jugador a añadir.
     */
    public void addPlayerToGame(Game game, Player player) {
        // Validar si el jugador ya está en el juego
        if (game.getPlayers().contains(player)) {
            throw new IllegalStateException("El jugador ya está en el juego.");
        }

        // Añadir el jugador al juego
        game.addPlayer(player);
        gameRepository.save(game); // Persistir el estado actualizado del juego
    }

    /**
     * Temporizador para iniciar un juego después de un tiempo de espera.
     *
     * @param game              El juego en espera de inicio.
     * @param waitTimeInSeconds Tiempo de espera en segundos.
     */
    public void startLobby(Game game, int waitTimeInSeconds) {
        new Thread(() -> {
            try {
                notificationController.notifyGameStarted("El lobby comenzará en " + waitTimeInSeconds + " segundos.");

                // Temporizador para el lobby
                Thread.sleep(waitTimeInSeconds * 1000);

                // Verifica si el juego sigue válido
                Game updatedGame = getGameById(game.getId());
                if (updatedGame.getPlayers().size() > 0) {
                    // Iniciar el juego
                    updatedGame.setActive(true);
                    gameRepository.save(updatedGame);
                    notificationController.notifyGameStarted("¡El juego ha comenzado!");
                } else {
                    // Finalizar el juego por falta de jugadores
                    endGame(updatedGame);
                    notificationController.notifyGameEnded("No se unieron jugadores. El juego ha terminado.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                notificationController.notifyGameEnded("El lobby fue interrumpido antes de comenzar.");
            }
        }).start();
    }
}



