package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.stubs;

import lombok.Getter;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "big_entity")
public class FakeEntityBigEntity extends ApigenAbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigInteger bInt;
    private BigDecimal bDec;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isReference() {
        return getId() != null;
    }
}
