package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatiereProduit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatiereProduitRepository extends JpaRepository<MatiereProduit, Long> {
    @Query("SELECT mp FROM MatiereProduit mp WHERE mp.produit.id = :produitId")
    List<MatiereProduit> findByProduit(@Param("produitId") Long produitId);
    @Query("SELECT mp FROM MatiereProduit mp WHERE mp.produit = :produit AND mp.matPremiere = :matPremiere")
    MatiereProduit findByProduitAndMatPremiere(@Param("produit") Produit produit, @Param("matPremiere") MatPremiere matPremiere);
    @Transactional
    @Modifying
    @Query("update MatiereProduit mp set mp.quantite = :quantite where mp.produit = :produit and mp.matPremiere = :matPremiere")
    void updateQuantite(@Param("produit") Produit produit, @Param("matPremiere") MatPremiere matPremiere, @Param("quantite") double quantite);

    @Transactional
    @Modifying
    @Query("delete from MatiereProduit mp where mp.produit = :produit")
    void deleteByProduit(@Param("produit") Produit produit);

}