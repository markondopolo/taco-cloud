package tacos.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;


@Data
@Entity
@Table(name = "Taco")
@ToString(exclude = "tacoOrder")
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @NotNull
    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany
    @JoinTable(
            name = "Ingredient_Ref",
            joinColumns = @JoinColumn(name = "taco"),
            inverseJoinColumns = @JoinColumn(name = "ingredient")
    )
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "taco_order_id")
    private TacoOrder tacoOrder;
}