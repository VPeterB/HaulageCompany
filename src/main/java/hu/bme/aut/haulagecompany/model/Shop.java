package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long locationId;  // You can change the type according to your actual use case
    private String contact;
}