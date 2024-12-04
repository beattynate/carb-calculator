package carb.calculator.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Meal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mealId;
	private String mealName;
	private BigDecimal totalWeight;
	private BigDecimal totalCarbGrams;
	private BigDecimal carbsPer100;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "container_id")
	private Container container;
	
		
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "meal_record_meal",
			joinColumns = @JoinColumn(name = "meal_id"),
			inverseJoinColumns = @JoinColumn(name = "meal_record_id"))
		private Set<MealRecord> mealRecords = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany (mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
		private Set<MealIngredient> mealIngredients = new HashSet<>();
	
	
	
}
