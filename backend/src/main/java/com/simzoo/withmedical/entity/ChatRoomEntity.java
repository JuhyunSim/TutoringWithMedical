package com.simzoo.withmedical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity participant1;
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity participant2;

    private Long createdBy;
}
