package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository;


import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeOperationRepository extends JpaRepository<TypeOperation, Long> {
    @Query("select t from TypeOperation t " +
            "where lower(t.des) like lower(concat('%', :searchTerm, '%')) ")
    List<TypeOperation> search(@Param("searchTerm") String searchTerm);
}
