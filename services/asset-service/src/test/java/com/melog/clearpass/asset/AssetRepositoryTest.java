package com.melog.clearpass.asset;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;      // AssertJ fluent assertions

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.melog.clearpass.asset.model.Asset;
import com.melog.clearpass.asset.repository.AssetRepository;
import com.melog.clearpass.common.ClearanceLevel;


@DisplayName("JPA slice – AssetRepository")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // uses H2 from runtime deps
class AssetRepositoryTest {

    @Autowired AssetRepository repo;

    @Test
    void saveAndFindById() {
        Asset saved = repo.save(
            Asset.builder()
                .serial("A-001")
                .description("Test asset")
                .requiredClearance(ClearanceLevel.UNCLASSIFIED)
                .userId(1L)
                .build()
        );
        assertThat(saved.getId()).isNotNull();

        Optional<Asset> fetched = repo.findById(saved.getId());
        assertThat(fetched)
            .isPresent()
            .get()
            .extracting(Asset::getSerial)
            .isEqualTo("A-001");
    }

    @Test
    void updateAsset() {
        Asset saved = repo.save(new Asset(null, "A-010", "Original", ClearanceLevel.UNCLASSIFIED, 1L));

        saved.setDescription("Updated desc");
        repo.save(saved);                    // JPA treats this as update

        Optional<Asset> reloaded = repo.findById(saved.getId());
        assertThat(reloaded).isPresent()
                            .get()
                            .extracting(Asset::getDescription)
                            .isEqualTo("Updated desc");
    }

    @Test
    void deleteAsset() {
        Asset saved = repo.save(new Asset(null, "A-011", "To-delete", ClearanceLevel.CONFIDENTIAL, 1L));

        repo.deleteById(saved.getId());

        assertThat(repo.findById(saved.getId())).isNotPresent();
    }
}