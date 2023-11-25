package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licensePlate;
    private Double size;
    private Double maxWeight;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LorrySite location;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<TransportOperation> transportOperations;
}
