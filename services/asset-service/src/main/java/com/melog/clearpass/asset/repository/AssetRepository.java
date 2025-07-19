package com.melog.clearpass.asset.repository;

import com.melog.clearpass.asset.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> { 
    boolean existsBySerial(String serial);
 }
