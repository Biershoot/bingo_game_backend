package service;

import model.Game;
import model.Player;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PlayerRepository;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player generateCard(User user, Game game) {
        Set<Integer> card = generateRandomCard();
        Player player = Player.builder()
                .user(user)
                .game(game)
                .card(card.toString())
                .build();
        return playerRepository.save(player);
    }

    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Jugador con ID " + playerId + " no encontrado."));
    }

    private Set<Integer> generateRandomCard() {
        // Lógica para generar un tarjetón único de Bingo con 25 números aleatorios
        Set<Integer> card = new HashSet<>();
        Random random = new Random();
        while (card.size() < 25) {
            int number = random.nextInt(75) + 1; // Números de 1 a 75
            card.add(number);
        }
        return card;
    }
}
