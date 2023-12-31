package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    @Query("select c from Commande c " +
            "where lower(c.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.ref) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(c.statut) like lower(concat('%', :searchTerm, '%'))")
    List<Commande> search(@Param("searchTerm") String searchTerm);

    //cette requête permet de récupérer toutes les commandes en cours
    @Query("select c from Commande c where c.statut = 'En attente'")
    List<Commande> findAllCommandeEnAttente();

    @Query("select c from Commande c where c.statut = 'En cours'")
    List<Commande> findAllCommandeEnCours();

    @Transactional
    @Modifying
    @Query("UPDATE Commande c SET c.statut = :statut WHERE c = :commande")
    void setCommandeEnProduction(Commande commande, String statut);
}
