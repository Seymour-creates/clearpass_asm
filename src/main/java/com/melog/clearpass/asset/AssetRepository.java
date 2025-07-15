package com.melog.clearpass.asset;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> { 
    boolean existsBySerial(String serial);
 }
