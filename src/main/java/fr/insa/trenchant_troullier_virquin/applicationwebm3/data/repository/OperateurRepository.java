package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperateurRepository extends JpaRepository<Operateur, Long> {


    @Query("select o from Operateur o " +
            "where lower(o.nom) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(o.prenom) like lower(concat('%', :searchTerm, '%'))")
    List<Operateur> search(@Param("searchTerm") String searchTerm);
}

