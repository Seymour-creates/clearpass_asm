package com.melog.clearpass.asset.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.melog.clearpass.asset.exception.AssetSerialAlreadyExistsException;
import com.melog.clearpass.common.exception.ResourceNotFoundException;
import com.melog.clearpass.asset.model.Asset;
import com.melog.clearpass.asset.repository.AssetRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AssetService {
    
    private final AssetRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;
    
    public AssetService(AssetRepository repo) { this.repo = repo; }

    public Asset create(Asset asset) {
        // Extract userId from JWT token stored in request attributes
        Long userId = getUserIdFromRequest();
        if (userId == null) {
            throw new RuntimeException("User ID not found in token");
        }
        
        // Set the userId from the token
        asset.setUserId(userId);
        
        // Validate user exists via user-service
        String url = userServiceUrl + "/api/users/" + userId;
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User", userId);
        }
        
        if (repo.existsBySerial(asset.getSerial())) {
            throw new AssetSerialAlreadyExistsException(asset.getSerial());
        }
        return repo.save(asset);
    }

    private Long getUserIdFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getAttribute("userId");
            if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
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
