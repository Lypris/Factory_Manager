package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EtatMachineRepository extends JpaRepository<EtatMachine, Long> {
    @Query("SELECT e FROM EtatMachine e JOIN e.machine m " +
            "WHERE lower(m.ref) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(m.des) like lower(concat('%', :searchTerm, '%'))")
    List<EtatMachine> search(@Param("searchTerm") String searchTerm);


    List<EtatMachine> findByMachine(Machine machine);

    List<EtatMachine> findAllByMachineAndDebutBeforeAndFinAfter(Machine machine, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT e FROM EtatMachine e WHERE e.fin is null")
    List<EtatMachine> findAllByFinIsNull();

    @Query("SELECT e FROM EtatMachine e JOIN e.machine m " +
            "WHERE lower(m.ref) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(m.des) like lower(concat('%', :searchTerm, '%'))"
            + "AND e.fin is null")
    List<EtatMachine> searchLast(@Param("searchTerm") String searchTerm);

    @Transactional
    @Modifying
    @Query("UPDATE EtatMachine e SET e.fin = :fin WHERE e = :etatmachine")
    void SetFinByEtatMachine(LocalDateTime fin, EtatMachine etatmachine);


    @Query("SELECT e FROM EtatMachine e WHERE e.machine = :machine AND e.fin = null")
    EtatMachine findCurrentEtatMachineByMachine(Machine machine);

    //on cherche l'état précédent de la machine
    @Query("SELECT e FROM EtatMachine e WHERE e.machine = :machine AND e.fin is not null ORDER BY e.fin DESC")
    List<EtatMachine> findPreviousEtatMachineByMachine(Machine machine);
    
    /*@Transactional
    @Modifying
    @Query("UPDATE EtatMachine e SET e = :etat WHERE e.machine = :machine")
    public void SetEtatMachineByMachine(Machine machine, EtatMachine etat);*/
}
