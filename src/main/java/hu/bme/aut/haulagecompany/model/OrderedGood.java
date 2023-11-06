package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OrderedGood extends Good{
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;
}
