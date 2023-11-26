package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.StackedGood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StackedGoodRepository extends CrudRepository<StackedGood, Long> {
    List<StackedGood> findAllByGoodId(Long id);
}
