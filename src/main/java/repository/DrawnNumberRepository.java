package repository;

import model.DrawnNumber;
import model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrawnNumberRepository extends JpaRepository<DrawnNumber, Long> {
    // Contar balotas extraídas para un juego específico
    long countByGame(Game game);

    // Buscar todas las balotas extraídas de un juego específico
    List<DrawnNumber> findByGame(Game game);
}
