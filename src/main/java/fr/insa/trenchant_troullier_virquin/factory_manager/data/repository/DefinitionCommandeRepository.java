package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface DefinitionCommandeRepository extends JpaRepository<DefinitionCommande, Long> {

    @Query("SELECT d FROM DefinitionCommande d " +
            "JOIN Commande c ON d.commande.id = c.id " +
            "JOIN Produit p ON d.produit.id = p.id " +
            "WHERE CAST(c.id AS string) LIKE CONCAT('%', :searchTerm, '%')")
    List<DefinitionCommande> findByIdCommande(@Param("searchTerm") Long searchTerm);

    @Query("SELECT d FROM DefinitionCommande d WHERE d.produit = :produit")
    List<DefinitionCommande> findByProduit(Produit produit);

    @Query("SELECT d FROM DefinitionCommande d WHERE d.produit = :produit AND d.commande = :commande")
    DefinitionCommande findDefinitionByProduitAndCommande(Produit produit, Commande commande);

    @Query("SELECT d FROM DefinitionCommande d WHERE d.produit = :produit AND d.commande = :commande")
    List<DefinitionCommande> findAllDefinitionByProduitAndCommande(Produit produit, Commande commande);

    @Modifying
    @Transactional
    @Query("DELETE FROM DefinitionCommande d WHERE d.commande = :commande")
    void deleteAllDefinitionByCommande(@Param("commande") Commande commande);

    @Modifying
    @Transactional
    @Query("DELETE FROM DefinitionCommande d WHERE d.produit = :produit")
    void deleteAllDefinitionByProduit(Produit produit);
}