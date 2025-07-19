package com.melog.clearpass.asset.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.melog.clearpass.asset.exception.AssetSerialAlreadyExistsException;
import com.melog.clearpass.common.exception.ResourceNotFoundException;
import com.melog.clearpass.asset.model.Asset;
import com.melog.clearpass.asset.repository.AssetRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AssetService {
    
    private final AssetRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;
    
    public AssetService(AssetRepository repo) { this.repo = repo; }

    public Asset create(Asset a) {
        // Validate user exists via user-service
        String url = userServiceUrl + "/api/users/" + a.getUserId();
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User", a.getUserId());
        }
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
