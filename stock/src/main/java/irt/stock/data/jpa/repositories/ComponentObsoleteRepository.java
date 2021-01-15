/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  irt.stock.data.jpa.beans.ComponentObsolete
 *  irt.stock.data.jpa.repositories.ComponentObsoleteRepository
 *  org.springframework.data.repository.CrudRepository
 */
package irt.stock.data.jpa.repositories;

import irt.stock.data.jpa.beans.ComponentObsolete;
import org.springframework.data.repository.CrudRepository;

public interface ComponentObsoleteRepository
extends CrudRepository<ComponentObsolete, Long> {
}

