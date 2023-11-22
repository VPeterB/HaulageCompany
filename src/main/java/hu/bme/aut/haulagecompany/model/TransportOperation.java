package hu.bme.aut.haulagecompany.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "transport_operations")
public class TransportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Vehicle> usedVehicles;
    @OneToOne
    @Nullable
    private Order order;

    public List<Long> getUsedVehicleIds() {
        List<Long> ids = new ArrayList<>();
        for(Vehicle v : this.usedVehicles){
            ids.add(v.getId());
        }
        return ids;
    }
}
