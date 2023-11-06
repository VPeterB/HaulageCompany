package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @OneToMany(mappedBy="order")
    private List<OrderedGood> orderedGoods;
    @OneToOne
    private TransportOperation transportOperation;
}
