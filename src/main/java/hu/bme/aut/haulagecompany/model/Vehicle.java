package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String licensePlate;

    // Constructors, getters, and setters
}
