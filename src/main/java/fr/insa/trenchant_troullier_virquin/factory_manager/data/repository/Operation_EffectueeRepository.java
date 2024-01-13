/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.factory_manager.data.repository;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operation_Effectuee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author laelt
 */
public interface Operation_EffectueeRepository extends JpaRepository<Operation_Effectuee, Long> {
    @Query("SELECT opf FROM Operation_Effectuee opf WHERE opf.exemplaire = :exemplaire AND opf.operation = :operation")
    List<Operation_Effectuee> OperationEffectueeExiste(Exemplaires exemplaire, Operation operation);

    void deleteAllOperationEffectueeByExemplaire(Exemplaires exemplaire);

    @Query("SELECT opf FROM Operation_Effectuee opf WHERE opf.exemplaire = :exemplaire")
    List<Operation_Effectuee> findByExemplaire(Exemplaires exemplaire);

    @Transactional
    @Modifying
    @Query("UPDATE Operation_Effectuee opf SET opf.fin = :time WHERE opf = :opf")
    void SetFinOperationEffectuee(LocalDateTime time, Operation_Effectuee opf);
}
