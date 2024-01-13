package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query("select p from Produit p " +
            "where lower(p.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.ref) like lower(concat('%', :searchTerm, '%'))")
    List<Produit> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Produit p " +
            "JOIN DefinitionCommande d ON d.produit.id = p.id " +
            "JOIN Commande c ON c.id = d.commande.id " +
            "WHERE c = :commande")
    List<Produit> findByCommande(Commande commande);
    
    @Query("SELECT p FROM Produit p WHERE p.id = :ID")
    Produit findProduitByID(long ID);
}
