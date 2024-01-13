package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatPossibleMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EtatPossibleMachineRepository extends JpaRepository<EtatPossibleMachine, Long> {

    @Query("SELECT e FROM EtatPossibleMachine e WHERE e.id = :id")
    EtatPossibleMachine findEtatPossibleById(long id);

    @Query("SELECT e FROM EtatPossibleMachine e WHERE e.des = :des")
    EtatPossibleMachine findEtatPossibleByDes(String des);

    @Query("SELECT ep FROM EtatPossibleMachine ep WHERE ep.des = 'en marche'")
    EtatPossibleMachine findEtatEnMarche();

    @Query("SELECT ep FROM EtatPossibleMachine ep WHERE ep.des = 'disponible'")
    EtatPossibleMachine findEtatDisponible();

}
