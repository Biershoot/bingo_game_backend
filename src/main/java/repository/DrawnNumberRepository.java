package repository;

import model.DrawnNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawnNumberRepository extends JpaRepository<DrawnNumber, Long> {}
