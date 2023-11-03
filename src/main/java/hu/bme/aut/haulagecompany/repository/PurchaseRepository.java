package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    // Custom queries can be added here if needed
}
