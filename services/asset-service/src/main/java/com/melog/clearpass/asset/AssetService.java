package com.melog.clearpass.asset;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.melog.clearpass.asset.exception.AssetSerialAlreadyExistsException;
import com.melog.clearpass.common.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AssetService {
    
    private final AssetRepository repo;
    
    public AssetService(AssetRepository repo) { this.repo = repo; }

    public Asset create(Asset a) { 
        if (repo.existsBySerial(a.getSerial())) {
            throw new AssetSerialAlreadyExistsException(a.getSerial());
        }
        return repo.save(a);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public List<Asset> findAll() { return repo.findAll(); }

    public Asset findById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asset", id)); }
    
    public Asset update(Long id, Asset newData) {
        Asset a = findById(id);
        a.setSerial(newData.getSerial());
        a.setDescription(newData.getDescription());
        a.setRequiredClearance(newData.getRequiredClearance());
        return repo.save(a);
    }

    public Optional<Asset> getAssetById(Long id) {
        return repo.findById(id);
    }
}
