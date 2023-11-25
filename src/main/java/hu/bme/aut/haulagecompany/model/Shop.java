package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Shop extends Location{
    @OneToMany(mappedBy="shop")
    @ToString.Exclude
    private List<Order> orders;

    public List<Long> getOrderIDs() {
        List<Long> ids = new ArrayList<>();
        for(Order o : this.orders){
            ids.add(o.getId());
        }
        return ids;
    }
}