package carb.calculator.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Container {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long containerId;
	private String containerName;
	private BigDecimal containerWeight;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "container", cascade = CascadeType.PERSIST)
	private Set<Meal> meals = new HashSet<>();

}
