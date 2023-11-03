package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "transport_operations")
public class TransportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehicleId;
    private Long shopId;
    @OneToMany(mappedBy = "transportOperation")
    private List<TransportedGood> goods; // Define TransportedGood as a separate class

    // You can add additional fields as needed
}
