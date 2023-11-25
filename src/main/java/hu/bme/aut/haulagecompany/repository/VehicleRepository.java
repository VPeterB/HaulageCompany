package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
