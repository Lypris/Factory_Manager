package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DefinitionCommandeRepository extends JpaRepository<DefinitionCommande, Long> {
    @Query("SELECT d FROM DefinitionCommande d " +
            "JOIN Commande c ON d.commande.id = c.id " +
            "JOIN Produit p ON d.produit.id = p.id " +
            "WHERE CAST(c.id AS string) LIKE CONCAT('%', :searchTerm, '%')")
    List<DefinitionCommande> findByIdCommande(@Param("searchTerm") Long searchTerm);
}

