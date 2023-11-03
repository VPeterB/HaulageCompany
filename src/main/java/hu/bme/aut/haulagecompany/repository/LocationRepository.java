package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    // Custom queries can be added here if needed
}
