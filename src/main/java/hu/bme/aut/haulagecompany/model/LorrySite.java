package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class LorrySite extends Location{
    @OneToMany(mappedBy="location")
    private List<Vehicle> vehicles;
    @OneToMany
    private List<Good> goods;

    public List<Long> getVehicleIds() {
        List<Long> ids = new ArrayList<>();
        for(Vehicle v : this.vehicles){
            ids.add(v.getId());
        }
        return ids;
    }

    public List<Long> getGoodIds() {
        List<Long> ids = new ArrayList<>();
        for(Good g : this.goods){
            ids.add(g.getId());
        }
        return ids;
    }
}
