package irt.stock.data.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Manufacture;

public interface ManufactureRepository extends CrudRepository<Manufacture, String> {
}
