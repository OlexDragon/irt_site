/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  irt.stock.data.jpa.beans.ComponentObsolete
 *  irt.stock.data.jpa.beans.ComponentObsolete$Status
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.EnumType
 *  javax.persistence.Enumerated
 *  javax.persistence.Id
 *  javax.persistence.Table
 *  lombok.NonNull
 */
package irt.stock.data.jpa.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name="components_obsolete")
@NoArgsConstructor @RequiredArgsConstructor @Getter @ToString
public class ComponentObsolete {

	@Id
    @NonNull @Setter
    private Long componentsId;
    @Enumerated(value=EnumType.ORDINAL)
    @NonNull @Setter @Accessors(chain = true)
    private Status status;
    @Column(nullable=true, insertable=false, updatable=false)
    private Timestamp date;

    public enum Status {
    	ACTIVE,
    	OBSOLETE
	}
}

