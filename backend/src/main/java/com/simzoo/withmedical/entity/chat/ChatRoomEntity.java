package com.simzoo.withmedical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "chatRoom")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
public class ChatRoomEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(fetch = FetchType.LAZY)
    List<MemberEntity> members = new ArrayList<>();

    private Long createdBy;
}
