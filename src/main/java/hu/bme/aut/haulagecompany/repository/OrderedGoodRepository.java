package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.OrderedGood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedGoodRepository extends CrudRepository<OrderedGood, Long> {
    void deleteAllByGoodId(Long goodId);
}
