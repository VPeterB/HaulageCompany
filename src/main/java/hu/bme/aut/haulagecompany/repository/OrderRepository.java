package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    // Custom queries can be added here if needed
}
