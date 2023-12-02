package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitRepository  extends JpaRepository<Produit, Long> {
    @Query("select p from Produit p " +
            "where lower(p.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.ref) like lower(concat('%', :searchTerm, '%'))")
    List<Produit> search(@Param("searchTerm") String searchTerm);
}
