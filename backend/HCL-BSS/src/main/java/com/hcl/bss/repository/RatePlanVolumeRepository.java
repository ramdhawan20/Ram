package com.hcl.bss.repository;

import com.hcl.bss.domain.Address;
import com.hcl.bss.domain.RatePlanVolume;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author- Aditya gupta
 */
public interface RatePlanVolumeRepository extends JpaRepository<RatePlanVolume, Long> {

    @Query(value= "SELECT * from tb_rateplan_volume_master rpv where rpv.RATE_PLAN_UID=?1 ",nativeQuery = true)
    List<RatePlanVolume> findByRatePlan(Long ratePlanUid);
}
