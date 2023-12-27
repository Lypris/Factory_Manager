/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation_Effectuee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author laelt
 */
public interface Operation_EffectueeRepository extends JpaRepository<Operation_Effectuee, Long>{
    
}
