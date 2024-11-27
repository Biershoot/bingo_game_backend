package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>(); // Lista de jugadores conectados al juego (lobby)

    /**
     * Devuelve los números de las balotas extraídas para el juego.
     */
    public Set<Integer> getDrawnBalls() {
        return this.drawnNumbers.stream()
                .map(DrawnNumber::getNumber)
                .collect(Collectors.toSet());
    }

    /**
     * Añade un jugador al juego.
     */
    public void addPlayer(Player player) {
        if (!this.players.contains(player)) {
            this.players.add(player);
            player.setGame(this); // Asociar el jugador al juego
        }
    }

    /**
     * Elimina un jugador del juego.
     */
    public void removePlayer(Player player) {
        if (this.players.contains(player)) {
            this.players.remove(player);
            player.setGame(null); // Disociar el jugador del juego
        }
    }
}







