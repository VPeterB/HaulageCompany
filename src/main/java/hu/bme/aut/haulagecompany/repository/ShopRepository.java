package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    Optional<Shop> findByName(String name);
    // Custom queries can be added here if needed
}
