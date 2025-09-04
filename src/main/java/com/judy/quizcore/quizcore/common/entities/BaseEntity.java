package com.judy.quizcore.quizcore.common.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 기본 클래스
 * 생성일시와 수정일시를 자동으로 관리합니다.
 * JPA Auditing을 통해 엔티티 생성/수정 시점을 자동으로 기록합니다.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class BaseEntity {
    
    /**
     * 엔티티 생성일시
     * 엔티티가 처음 저장될 때 자동으로 설정되며, 이후 수정할 수 없습니다.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    /**
     * 엔티티 수정일시
     * 엔티티가 수정될 때마다 자동으로 업데이트됩니다.
     */
    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;
}
