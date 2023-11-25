package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    @Query("select m from Machine m " +
            "where lower(m.des) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(m.ref) like lower(concat('%', :searchTerm, '%'))")
    List<Machine> search(@Param("searchTerm") String searchTerm);
}
