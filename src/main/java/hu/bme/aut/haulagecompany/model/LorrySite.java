package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class LorrySite extends Location{
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "location", cascade = CascadeType.REMOVE)
    private List<Vehicle> vehicles;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "lorrySite", cascade = CascadeType.REMOVE)
    private List<InventoryGood> goods;
}
