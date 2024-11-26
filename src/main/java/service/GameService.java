package service;

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
}

