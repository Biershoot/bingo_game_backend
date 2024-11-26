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
    private final Random random = new Random();

    public BallDrawService() {
        // Inicializar lista vacía. Se llenará por juego.
    }

    /**
     * Inicializa las balotas disponibles para un juego.
     */
    public void initializeAvailableBalls(Game game) {
        if (drawnNumberRepository.countByGame(game) == 0) {
            for (int i = 1; i <= 75; i++) {
                availableBalls.add(i);
            }
        } else {
            List<Integer> drawnBalls = drawnNumberRepository.findByGame(game)
                    .stream()
                    .map(DrawnNumber::getNumber)
                    .toList();
            for (int i = 1; i <= 75; i++) {
                if (!drawnBalls.contains(i)) {
                    availableBalls.add(i);
                }
            }
        }
    }

    /**
     * Extrae una balota aleatoria y la guarda en la base de datos.
     */
    public synchronized int drawBall(Game game) {
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

    /**
     * Devuelve las balotas ya extraídas para un juego.
     */
    public List<Integer> getDrawnBalls(Game game) {
        return drawnNumberRepository.findByGame(game)
                .stream()
                .map(DrawnNumber::getNumber)
                .toList();
    }
}


