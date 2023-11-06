package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Shop extends Location{
    @OneToMany(mappedBy="shop")
    private List<Order> orders;
}