package hu.bme.aut.haulagecompany.model;

import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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
    @OneToMany
    private List<Good> goods;
    @OneToOne
    private TransportOperation transportOperation;
}
