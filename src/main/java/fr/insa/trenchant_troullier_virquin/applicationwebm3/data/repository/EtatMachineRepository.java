package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
