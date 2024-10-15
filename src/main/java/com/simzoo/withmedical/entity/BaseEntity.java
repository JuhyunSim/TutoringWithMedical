package com.simzoo.withmedical.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Audited
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
