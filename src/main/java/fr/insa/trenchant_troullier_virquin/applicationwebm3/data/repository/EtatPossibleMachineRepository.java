package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EtatPossibleMachineRepository extends JpaRepository<EtatPossibleMachine, Long> {

    @Query("SELECT e FROM EtatPossibleMachine e WHERE e.id = :id")
    public EtatPossibleMachine findEtatPossibleById(long id);
   
}
