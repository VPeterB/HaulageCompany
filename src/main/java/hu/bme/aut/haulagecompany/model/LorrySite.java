package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class LorrySite extends Location{
    @OneToMany(mappedBy="location")
    private List<Vehicle> vehicles;
    @OneToMany(mappedBy="location")
    private List<StoredGood> storedGoods;
}
