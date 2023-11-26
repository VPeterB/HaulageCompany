package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.InventoryGood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryGoodRepository extends CrudRepository<InventoryGood, Long> {
}
