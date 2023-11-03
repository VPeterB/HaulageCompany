package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long shopId;
    @OneToMany(mappedBy = "purchase")
    private List<PurchasedGood> goods; // Define PurchasedGood as a separate class

    // You can add additional fields as needed
}
