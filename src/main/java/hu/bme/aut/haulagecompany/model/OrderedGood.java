package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class OrderedGood extends StackedGood{
    @ManyToOne
    Order order;
}
