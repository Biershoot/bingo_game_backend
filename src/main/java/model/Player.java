package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // Usuario asociado al jugador

    @ManyToOne
    private Game game;

    @Lob
    @Column(nullable = false)
    private String card; // Tarjetón almacenado como JSON en la base de datos
}
