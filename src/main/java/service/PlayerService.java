package service;

import model.Game;
import model.Player;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PlayerRepository;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player generateCard(User user, Game game) {
        String card = generateRandomCard();
        Player player = Player.builder()
                .user(user)
                .game(game)
                .card(card)
                .build();
        return playerRepository.save(player);
    }

    private String generateRandomCard() {
        // Lógica para generar un tarjetón de bingo en formato JSON
        // Ejemplo simplificado
        return "{ \"B\": [1, 2, 3], \"I\": [16, 17, 18], \"N\": [31, 32, 33], \"G\": [46, 47, 48], \"O\": [61, 62, 63] }";
    }
}
