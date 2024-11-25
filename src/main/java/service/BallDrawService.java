package service;

import model.DrawnNumber;
import model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DrawnNumberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BallDrawService {
    @Autowired
    private DrawnNumberRepository drawnNumberRepository;

    private List<Integer> availableBalls = new ArrayList<>();
    private Random random = new Random();

    public BallDrawService() {
        // Inicializar las 75 balotas
        for (int i = 1; i <= 75; i++) {
            availableBalls.add(i);
        }
    }

    public int drawBall(Game game) {
        if (availableBalls.isEmpty()) {
            throw new RuntimeException("No quedan balotas disponibles");
        }
        int index = random.nextInt(availableBalls.size());
        int ball = availableBalls.remove(index);

        DrawnNumber drawnNumber = DrawnNumber.builder()
                .game(game)
                .number(ball)
                .build();
        drawnNumberRepository.save(drawnNumber);

        return ball;
    }
}

