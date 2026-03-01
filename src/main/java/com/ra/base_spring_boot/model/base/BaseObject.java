package com.ra.base_spring_boot.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseObject that = (BaseObject) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31; // tránh đổi hash khi id còn null
    }
}
