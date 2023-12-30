/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation_Effectuee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author laelt
 */
public interface Operation_EffectueeRepository extends JpaRepository<Operation_Effectuee, Long>{
    @Query("SELECT opf FROM Operation_Effectuee opf WHERE opf.exemplaire = :exemplaire AND opf.operation = :operation")
    List<Operation_Effectuee> OperationEffectueeExiste(Exemplaires exemplaire, Operation operation);
}
