package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class LorrySite extends Location{
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "location")
    private List<Vehicle> vehicles;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "lorrySite")
    private List<InventoryGood> goods;
}
