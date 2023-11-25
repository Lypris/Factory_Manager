package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtatPossibleMachineRepository extends JpaRepository<EtatPossibleMachine, Long> {
}
