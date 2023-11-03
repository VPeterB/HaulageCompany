package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.TransportOperation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportOperationRepository extends CrudRepository<TransportOperation, Long> {
    // Custom queries can be added here if needed
}
