package com.ra.base_spring_boot.model.auth;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "blacklist_token")
@Getter
@Setter
@NoArgsConstructor
public class BlacklistToken extends BaseObject {
    @Lob
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date expiredDate;
}
