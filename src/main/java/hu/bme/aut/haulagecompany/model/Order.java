package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<OrderedGood> goods;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private TransportOperation transportOperation;
}
