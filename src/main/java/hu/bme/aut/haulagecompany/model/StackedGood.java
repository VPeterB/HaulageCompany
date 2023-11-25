package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class StackedGood extends Good{
    private int quantity;
    LorrySite lorrySite;
}
