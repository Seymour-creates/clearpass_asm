package com.melog.clearpass.asset;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

// @RequiredArgsConstructor
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService svc;

    public AssetController(AssetService svc) { this.svc = svc; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Asset create(@Valid @RequestBody Asset asset) {
        return svc.create(asset);
    }

    @GetMapping
    public List<Asset> all() { return svc.findAll(); }

    @GetMapping("/{id}")
    public Asset one(@PathVariable Long id) { return svc.findById(id); }

    @PutMapping("/{id}")
    public Asset replace(@PathVariable Long id, @Valid @RequestBody Asset asset) {
        return svc.update(id, asset);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { svc.delete(id); }
}
