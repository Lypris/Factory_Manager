package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.PosteDeTravail;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    @Query("select m from Machine m " +
            "where lower(m.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(m.ref) like lower(concat('%', :searchTerm, '%'))")
    List<Machine> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT m FROM Machine m " +
            "JOIN EtatMachine em ON m.id = em.machine.id " +
            "JOIN EtatPossibleMachine epm ON em.etat.id = epm.id " +
            "WHERE epm.des = 'disponible'" +
            "AND (em.fin IS NULL OR em.fin > CURRENT_TIMESTAMP)")
    List<Machine> findAllMachineDisponibles();

    @Query("SELECT m FROM Machine m " +
            "JOIN EtatMachine em ON m.id = em.machine.id " +
            "JOIN EtatPossibleMachine epm ON em.etat.id = epm.id " +
            "WHERE epm.des = 'disponible'" +
            "AND em.fin IS NULL " +
            "AND m.typeOperation = :typeOperation")
    List<Machine> findAllMachineDisponiblesForTypeOperation(@Param("typeOperation") TypeOperation typeOperation);

    @Query("SELECT m FROM Machine m " +
            "WHERE m.posteDeTravail = :posteDeTravail")
    List<Machine> findByPosteDeTravail(@Param("posteDeTravail") PosteDeTravail posteDeTravail);
    
    @Query("SELECT m FROM Machine m "
            + "JOIN Operation_Effectuee opf ON opf.machine = m "
            + "WHERE opf.exemplaire = :exemplaire")
    List<Machine> findAllMachineByProduitAndCommande(Exemplaires exemplaire); 
}
