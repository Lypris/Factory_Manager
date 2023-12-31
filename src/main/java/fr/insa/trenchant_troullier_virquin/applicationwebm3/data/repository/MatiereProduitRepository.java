package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatiereProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatiereProduitRepository extends JpaRepository<MatiereProduit, Long> {
    @Query("SELECT mp FROM MatiereProduit mp WHERE mp.produit.id = :produitId")
    List<MatiereProduit> findByProduit(@Param("produitId") Long produitId);
}