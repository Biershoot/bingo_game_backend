package service;

import controller.NotificationController;
import model.Game;
import model.DrawnNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DrawnNumberRepository;


import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

@Service
public class BallDrawService {

    private final Random random = new Random();
    private final List<Integer> availableBalls;

    @Autowired
    private DrawnNumberRepository drawnNumberRepository;

    @Autowired
    private NotificationController notificationController;

    public BallDrawService() {
        this.availableBalls = initializeBalls();
    }

    private List<Integer> initializeBalls() {
        return IntStream.rangeClosed(1, 75).boxed().collect(Collectors.toList());
    }

    /**
     * Initializes the available balls for a game.
     */
    public synchronized void initializeAvailableBalls(Game game) {
        availableBalls.clear();
        List<Integer> drawnBalls = drawnNumberRepository.findByGame(game)
                .stream()
                .map(DrawnNumber::getNumber)
                .toList();

        availableBalls.addAll(IntStream.rangeClosed(1, 75)
                .filter(i -> !drawnBalls.contains(i))
                .boxed()
                .toList());
    }

    /**
     * Draws a random ball, saves it in the database, and sends a notification.
     */
    public synchronized int drawBall(Game game) {
        if (availableBalls.isEmpty()) {
            throw new RuntimeException("No quedan balotas disponibles.");
        }

        int index = random.nextInt(availableBalls.size());
        int ball = availableBalls.remove(index);

        // Save the drawn ball to the database
        DrawnNumber drawnNumber = DrawnNumber.builder()
                .game(game)
                .number(ball)
                .build();
        drawnNumberRepository.save(drawnNumber);

        // Notify ball draw
        notificationController.notifyBallDrawn(ball);

        return ball;
    }

    /**
     * Returns all drawn balls for a given game.
     */
    public List<Integer> getDrawnBalls(Game game) {
        return drawnNumberRepository.findByGame(game)
                .stream()
                .map(DrawnNumber::getNumber)
                .toList();
    }
}



