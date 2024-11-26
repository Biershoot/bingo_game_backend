package service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BingoVerificationService {

    public boolean isBingo(String card, Set<Integer> drawnBalls) {
        // Lógica para validar si el tarjetón (card) contiene un bingo válido
        // Ejemplo simplificado:
        // card es un JSON con números; drawnBalls contiene las balotas extraídas.
        // Implementa la lógica real según tu necesidad.
        return true; // Cambia esto con la lógica real
    }
}
