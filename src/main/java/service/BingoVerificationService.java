package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class BingoVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(BingoVerificationService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Verifica si el jugador tiene un Bingo válido.
     *
     * @param card       Tarjetón del jugador en formato JSON.
     * @param drawnBalls Conjunto de balotas extraídas.
     * @return true si el jugador tiene un Bingo válido; false en caso contrario.
     */
    public boolean isBingo(String card, Set<Integer> drawnBalls) {
        try {
            // Parsear el tarjetón
            Map<String, int[]> bingoCard = objectMapper.readValue(card, new TypeReference<>() {});

            // Validar horizontales
            if (hasHorizontalBingo(bingoCard, drawnBalls)) return true;

            // Validar verticales
            if (hasVerticalBingo(bingoCard, drawnBalls)) return true;

            // Validar diagonales
            return hasDiagonalBingo(bingoCard, drawnBalls);

            // Si no hay Bingo, devuelve false
        } catch (Exception e) {
            // Logging de errores de parsing
            logger.error("Error al procesar el tarjetón de Bingo.", e);
            return false;
        }
    }

    private boolean hasHorizontalBingo(Map<String, int[]> bingoCard, Set<Integer> drawnBalls) {
        for (int[] row : bingoCard.values()) {
            boolean isBingo = true;
            for (int number : row) {
                if (!drawnBalls.contains(number)) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) return true;
        }
        return false;
    }

    private boolean hasVerticalBingo(Map<String, int[]> bingoCard, Set<Integer> drawnBalls) {
        for (int i = 0; i < 5; i++) { // Asumiendo una cuadrícula 5x5
            boolean isBingo = true;
            for (int[] row : bingoCard.values()) {
                if (!drawnBalls.contains(row[i])) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) return true;
        }
        return false;
    }

    private boolean hasDiagonalBingo(Map<String, int[]> bingoCard, Set<Integer> drawnBalls) {
        boolean isBingo = true;

        // Diagonal principal (de arriba a la derecha)
        int i = 0;
        for (int[] row : bingoCard.values()) {
            if (!drawnBalls.contains(row[i])) {
                isBingo = false;
                break;
            }
            i++;
        }
        if (isBingo) return true;

        // Diagonal secundaria (de arriba a la izquierda)
        i = 4;
        isBingo = true;
        for (int[] row : bingoCard.values()) {
            if (!drawnBalls.contains(row[i])) {
                isBingo = false;
                break;
            }
            i--;
        }
        return isBingo;
    }
}


