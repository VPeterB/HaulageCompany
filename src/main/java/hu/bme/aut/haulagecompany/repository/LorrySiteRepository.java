package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Location;
import hu.bme.aut.haulagecompany.model.LorrySite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LorrySiteRepository extends CrudRepository<LorrySite, Long> {}
