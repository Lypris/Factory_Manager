package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatutOperateurRepository extends JpaRepository<StatutOperateur, Long> {

    @Query("SELECT so FROM StatutOperateur so JOIN so.operateur o " +
            "WHERE lower(o.nom) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(o.prenom) like lower(concat('%', :searchTerm, '%'))")
    List<StatutOperateur> search(@Param("searchTerm") String searchTerm);

    List<StatutOperateur> findByOperateur(Operateur operateur);
}
/*

    @Query("select r_operateur.nom, r_operateur.prenom, r_operateur.mail, r_operateur.tel, r_etat_possible_operateur.des from r_etat_operateur " +
            "join r_etat_possible_operateur on r_etat_operateur.idetatpossible = r_etat_possible_operateur.id " +
            "join r_operateur on r_etat_operateur.idoperateur = r_operateur.id " +
            "where lower(r_operateur.nom) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(r_operateur.prenom) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(r_operateur.mail) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(r_operateur.tel) like lower(concat('%', :searchTerm, '%'))")

      */
