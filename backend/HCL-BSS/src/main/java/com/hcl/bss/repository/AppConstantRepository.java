package com.hcl.bss.repository;


import com.hcl.bss.domain.AppConstantMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppConstantRepository extends JpaRepository<AppConstantMaster, Long> {

    @Query(value="select SUB_DROPDOWN_CODE from TB_APP_CONSTANTS_MASTER where DROPDOWN_CODE=?",nativeQuery=true)
	List<String> findByDropdownCode(String statusID);
}
