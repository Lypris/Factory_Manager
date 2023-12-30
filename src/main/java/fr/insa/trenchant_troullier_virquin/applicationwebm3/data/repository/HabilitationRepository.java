package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Habilitation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.PosteDeTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HabilitationRepository extends JpaRepository<Habilitation, Long> {
    @Query("SELECT o FROM Habilitation h " +
            "JOIN h.operateur o " +
            "WHERE h.posteDeTravail = :posteDeTravail")
    List<Operateur> findOperateursByPosteDeTravail(@Param("posteDeTravail") PosteDeTravail posteDeTravail);

    @Query("SELECT h FROM Habilitation h " +
            "WHERE h.posteDeTravail = :posteDeTravail")
    List<Habilitation> findAllHabilitationByPosteDeTravail(@Param("posteDeTravail") PosteDeTravail posteDeTravail);
}
