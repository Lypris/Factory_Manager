package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT o FROM Operation o WHERE o.produit.id = :produitId")
    List<Operation> findByProduitId(@Param("produitId") Long produitId);
    
    @Query("SELECT COUNT(*) FROM Operation o WHERE o.produit = :produit")
    int CountOperationByProduit (Produit produit);
    
    @Query("SELECT o FROM Operation o WHERE o.produit = :produit ORDER BY o.ordre LIMIT 1")
    public Operation findOneByProduit(Produit produit);
}
