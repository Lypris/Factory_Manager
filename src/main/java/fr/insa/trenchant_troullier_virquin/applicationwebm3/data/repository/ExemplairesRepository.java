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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author laelt
 */
public interface ExemplairesRepository extends JpaRepository<Exemplaires, Long>{
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > 0 AND e.etape < 100")
    List<Exemplaires> findAllProdEnCours();
    
    @Query("SELECT e FROM Exemplaires e WHERE e.etape > 99")
    List<Exemplaires> findAllProdFini();
    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande")
    List<Exemplaires> findByCommande(Commande commande);

    @Query("SELECT e FROM Exemplaires e WHERE e.produit = :produit")
    List<Exemplaires> findByProduit(Produit produit);

    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande and e.produit = :produit")
    int countExemplaires(Commande commande, Produit produit);

    @Query("SELECT e FROM Exemplaires e WHERE e.commande = :commande and e.produit = :produit and e.etape > 0")
    List <Exemplaires>findAllExemplaireEnCoursByProduitAndCommande(Produit produit, Commande commande);
}
