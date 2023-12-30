package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Habilitation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.PosteDeTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HabilitationRepository extends JpaRepository<Habilitation, Long> {
    @Query ("SELECT h FROM Habilitation h " +
            "JOIN Operateur o ON o.id = h.operateur.id " +
            "WHERE h.posteDeTravail = :posteDeTravail")

    List<Operateur> findByPosteDeTravail(PosteDeTravail posteDeTravail);
}
