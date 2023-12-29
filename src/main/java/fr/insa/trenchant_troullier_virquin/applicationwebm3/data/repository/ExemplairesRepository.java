/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;

import java.util.ArrayList;
import java.util.List;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 *
 * @author laelt
 */
public interface ExemplairesRepository extends JpaRepository<Exemplaires, Long>{
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > 0 AND e.etape < 100")
    List<Exemplaires> findAllProdEnCours();

    @Query("SELECT e FROM Exemplaires e WHERE e.etape =null ")
    List<Exemplaires> findAllProdFini();
    //méthode pour trouver les exemplaires finis d'un produit donné et d'une commande donnée
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > :etapes AND e.produit = :produit AND e.commande = :commande")
    List<Exemplaires> findAllProdFiniByProduitAndCommande(Produit produit, Commande commande, int etapes);
    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande")
    List<Exemplaires> findByCommande(Commande commande);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Exemplaires e WHERE e.commande = :commande")
    void deleteAllExemplaireByCommande(Commande commande);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Exemplaires e WHERE e.produit = :produit and e.etape = 0")
    void deleteAllExemplaireEnAttenteByProduit(Produit produit);

    @Query("SELECT COUNT(e) FROM Exemplaires e WHERE e.commande = :commande and e.produit = :produit")
    int countExemplairesByCommandeAndProduit(Commande commande, Produit produit);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM exemplaires e WHERE e.commande_id = :commandeId AND e.produit_id = :produitId LIMIT :n", nativeQuery = true)
    void deleteNExemplaireByProduitAndCommande(@Param("n") int n, @Param("produitId") long produitId, @Param("commandeId") Long commandeId);
}
