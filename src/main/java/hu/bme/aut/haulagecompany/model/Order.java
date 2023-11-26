package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private List<OrderedGood> goods;

    @OneToOne
    private TransportOperation transportOperation;
}
