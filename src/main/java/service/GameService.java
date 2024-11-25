package service;

import model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GameRepository;

import java.util.Date;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game startGame() {
        Game game = Game.builder()
                .createdAt(new Date().toString())
                .build();
        return gameRepository.save(game);
    }
}
