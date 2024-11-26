package controller;

import model.Game;
import model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BingoVerificationService;
import service.GameService;
import service.PlayerService;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final BingoVerificationService bingoVerificationService;

    // Inyección por constructor (mejor práctica)
    public GameController(GameService gameService, PlayerService playerService, BingoVerificationService bingoVerificationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.bingoVerificationService = bingoVerificationService;
    }

    /**
     * Inicia un nuevo juego.
     *
     * @return El juego recién creado.
     */
    @PostMapping("/start")
    public ResponseEntity<Game> startGame() {
        Game game = gameService.startGame();
        return ResponseEntity.ok(game);
    }

    /**
     * Finaliza un juego existente.
     *
     * @param gameId ID del juego a finalizar.
     * @return Mensaje de confirmación.
     */
    @PostMapping("/{gameId}/end")
    public ResponseEntity<String> endGame(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        gameService.endGame(game);
        return ResponseEntity.ok("El juego ha sido finalizado.");
    }

    /**
     * Declara un ganador para el juego.
     *
     * @param gameId   ID del juego.
     * @param playerId ID del jugador declarado ganador.
     * @return Mensaje de confirmación.
     */
    @PostMapping("/{gameId}/bingo")
    public ResponseEntity<String> declareBingo(@PathVariable Long gameId, @RequestParam Long playerId) {
        // Obtener el juego por ID
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return ResponseEntity.badRequest().body("El juego no existe.");
        }

        // Obtener el jugador y su tarjetón
        Player player = playerService.getPlayerById(playerId);
        if (player == null) {
            return ResponseEntity.badRequest().body("El jugador no existe.");
        }

        // Validar Bingo
        boolean isValidBingo = bingoVerificationService.isBingo(player.getCard(), game.getDrawnBalls());
        if (isValidBingo) {
            // Declarar el jugador como ganador del juego
            gameService.declareWinner(game, player);
            return ResponseEntity.ok("¡Bingo válido! Jugador " + player.getUser().getUsername() + " ha ganado.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("¡Bingo inválido! Jugador " + player.getUser().getUsername() + " es descalificado.");
        }
    }
}

