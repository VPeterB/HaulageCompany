package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Good;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository<T extends Good> extends CrudRepository<T, Long> {
    // Custom queries can be added here if needed
}