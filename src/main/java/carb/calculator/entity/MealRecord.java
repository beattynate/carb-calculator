package carb.calculator.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class MealRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mealRecordId;
	private BigDecimal portionWeight;
	private BigDecimal portionCarbs;
	private BigDecimal mealTotalWeight;
	private BigDecimal mealTotalCarbs;
	private LocalDateTime timestamp;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "mealRecords")
		private Set<Meal> meals = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "mealRecords")
		private Set<Ingredient> ingredients = new HashSet<>();
}
