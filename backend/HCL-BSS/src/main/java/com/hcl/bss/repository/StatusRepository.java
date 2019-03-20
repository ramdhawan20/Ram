package com.hcl.bss.repository;


import com.hcl.bss.domain.StatusDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<StatusDetails, Long> {

    List<StatusDetails> findByStatusId(Integer statusID);
}
