package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StackedGood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "good_id")
    Good good;
}
