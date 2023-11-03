package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TransportedGood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TransportOperation transportOperation;
    private Long goodId;
    private int quantity;

    // You can add additional fields as needed
}
