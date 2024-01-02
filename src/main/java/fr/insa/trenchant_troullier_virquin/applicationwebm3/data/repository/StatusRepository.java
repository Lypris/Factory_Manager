package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatusRepository extends JpaRepository<Statut, Long> {
    @Query("SELECT s FROM Statut s WHERE s.name = :des")
    Statut findStatutPossibleByDes(String des);
}
