package com.melog.clearpass.asset.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.melog.clearpass.common.ClearanceLevel;

@Data
@Entity
@Table(name = "assets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String serial;

    @NotBlank
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClearanceLevel requiredClearance;

    @NotNull
    private Long userId;
}
