package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "goods")
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;  // You can change the type according to your actual use case
}
