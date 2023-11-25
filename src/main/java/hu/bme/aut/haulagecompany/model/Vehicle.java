package hu.bme.aut.haulagecompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TransportOperation> transportOperations;

    public List<Long> getTransportOperationIds() {
        List<Long> ids = new ArrayList<>();
        for(TransportOperation to : this.transportOperations){
            ids.add(to.getId());
        }
        return ids;
    }
}
