package controller;

import model.Game;
import model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BingoVerificationService;
import service.GameService;
import service.PlayerService;
import controller.NotificationController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final BingoVerificationService bingoVerificationService;
    private final NotificationController notificationController;

    // Dependency Injection via constructor
    public GameController(GameService gameService, PlayerService playerService,
                          BingoVerificationService bingoVerificationService,
                          NotificationController notificationController) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.bingoVerificationService = bingoVerificationService;
        this.notificationController = notificationController;
    }

    /**
     * Starts a new game.
     *
     * @return The newly created game.
     */
    @PostMapping("/start")
    public ResponseEntity<Game> startGame() {
        try {
            Game game = gameService.startGame();
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Ends an existing game.
     *
     * @param gameId ID of the game to end.
     * @return Confirmation message.
     */
    @PostMapping("/{gameId}/end")
    public ResponseEntity<String> endGame(@PathVariable Long gameId) {
        try {
            Game game = gameService.getGameById(gameId);
            if (!game.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El juego ya ha finalizado.");
            }
            gameService.endGame(game);
            return ResponseEntity.ok("El juego ha sido finalizado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al finalizar el juego.");
        }
    }

    /**
     * Declares a Bingo for a player.
     *
     * @param gameId   ID of the game.
     * @param playerId ID of the player declaring Bingo.
     * @return Confirmation message.
     */
    @PostMapping("/{gameId}/bingo")
    public ResponseEntity<String> declareBingo(@PathVariable Long gameId, @RequestParam Long playerId) {
        try {
            Game game = gameService.getGameById(gameId);
            if (!game.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El juego ya ha finalizado.");
            }

            Player player = playerService.getPlayerById(playerId);
            if (player == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El jugador no existe.");
            }

            boolean isValidBingo = bingoVerificationService.isBingo(player.getCard(), game.getDrawnBalls());
            if (isValidBingo) {
                gameService.declareWinner(game, player);
                return ResponseEntity.ok("¡Bingo válido! Jugador " + player.getUser().getUsername() + " ha ganado.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("¡Bingo inválido! Jugador " + player.getUser().getUsername() + " es descalificado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la declaración de Bingo.");
        }
    }

    /**
     * Allows a player to join a game lobby.
     *
     * @param gameId   ID of the game.
     * @param playerId ID of the player.
     * @return Confirmation message.
     */
    @PostMapping("/{gameId}/join")
    public ResponseEntity<String> joinGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        try {
            // Validate game
            Game game = gameService.getGameById(gameId);
            if (!game.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El juego no está activo.");
            }

            // Validate player
            Player player = playerService.getPlayerById(playerId);
            if (player == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El jugador no existe.");
            }

            // Add player to the game
            gameService.addPlayerToGame(game, player);

            // Notify other players
            notificationController.notifyPlayerJoined(player.getUser().getUsername());
            return ResponseEntity.ok("Jugador " + player.getUser().getUsername() + " se ha unido al juego.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al unir al jugador al juego.");
        }
    }
}


