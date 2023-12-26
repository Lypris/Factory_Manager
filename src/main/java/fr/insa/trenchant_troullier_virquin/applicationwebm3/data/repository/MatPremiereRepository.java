/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatPremiere;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author laelt
 */
public interface MatPremiereRepository extends JpaRepository<MatPremiere, Long> {
    @Query("select p from Produit p " +
            "where lower(p.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.ref) like lower(concat('%', :searchTerm, '%'))")
    List<MatPremiere> search(@Param("searchTerm") String searchTerm);
}
