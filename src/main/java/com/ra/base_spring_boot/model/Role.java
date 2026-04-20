package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "role",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_name", columnNames = "role_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseObject {
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

}
