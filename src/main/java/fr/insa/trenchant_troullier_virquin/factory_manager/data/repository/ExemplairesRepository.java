/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author laelt
 */
public interface ExemplairesRepository extends JpaRepository<Exemplaires, Long> {
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > 0 AND e.etape < 100")
    List<Exemplaires> findAllProdEnCours();

    @Query("SELECT e FROM Exemplaires e WHERE e.etape =null ")
    List<Exemplaires> findAllProdFini();

    //méthode pour trouver les exemplaires finis d'un produit donné et d'une commande donnée
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > :etapes AND e.produit = :produit AND e.commande = :commande")
    List<Exemplaires> findAllProdFiniByProduitAndCommande(Produit produit, Commande commande, int etapes);

    @Query("SELECT e FROM Exemplaires e WHERE e.etape <= :etapes AND e.produit = :produit AND e.commande = :commande")
    List<Exemplaires> findAllProdEnCoursByProduitAndCommande(Produit produit, Commande commande, int etapes);

    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande")
    List<Exemplaires> findByCommande(Commande commande);

    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande AND e.produit = :produit")
    List<Exemplaires> findByCommandeAndProduit(Commande commande, Produit produit);
    
    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande AND e.produit = :produit ORDER BY e.id LIMIT 1")
    Exemplaires findONEByCommandeAndProduit(Commande commande, Produit produit);
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Exemplaires e WHERE e.commande = :commande")
    void deleteAllExemplaireByCommande(Commande commande);

    @Transactional
    @Modifying
    @Query("UPDATE Exemplaires e SET e.etape = :nbOpe WHERE e = :exemplaire")
    void ExemplaireFini(int nbOpe, Exemplaires exemplaire);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Exemplaires e WHERE e.produit = :produit and e.etape = 0")
    void deleteAllExemplaireEnAttenteByProduit(Produit produit);

    @Query("SELECT COUNT(e) FROM Exemplaires e WHERE e.commande = :commande and e.produit = :produit")
    int countExemplairesByCommandeAndProduit(Commande commande, Produit produit);
    
    @Query("SELECT COUNT(*) FROM Exemplaires e WHERE e.etape > :nbOpe AND e.commande = :commande and e.produit = :produit")
    int countProdFiniByCommandeAndProduit(Commande commande, Produit produit, int nbOpe);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM exemplaires e WHERE e.commande_id = :commandeId AND e.produit_id = :produitId LIMIT :n", nativeQuery = true)
    void deleteNExemplaireByProduitAndCommande(@Param("n") int n, @Param("produitId") long produitId, @Param("commandeId") Long commandeId);
}
