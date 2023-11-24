package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.TransportOperation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportOperationRepository extends CrudRepository<TransportOperation, Long> {
    Optional<TransportOperation> findByOrderId(Long orderID);
}
