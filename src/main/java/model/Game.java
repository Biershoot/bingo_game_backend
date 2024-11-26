package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt; // Fecha y hora del inicio del juego

    @ManyToOne
    private User winner; // Usuario que ganó el juego

    @Column(nullable = false)
    private boolean active; // Indica si el juego está activo o finalizado

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DrawnNumber> drawnNumbers; // Balotas extraídas asociadas al juego

    /**
     * Devuelve los números de las balotas extraídas para el juego.
     */
    public Set<Integer> getDrawnBalls() {
        return this.drawnNumbers.stream()
                .map(DrawnNumber::getNumber)
                .collect(Collectors.toSet());
    }
}





