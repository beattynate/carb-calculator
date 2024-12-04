package carb.calculator.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class MealIngredient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long mealIngredientId;
		private BigDecimal ingredientGrams;
		
		@EqualsAndHashCode.Exclude
		@ToString.Exclude
		@ManyToOne
		@JoinColumn(name = "meal_id")
		private Meal meal;
		
		@EqualsAndHashCode.Exclude
		@ToString.Exclude
		@ManyToOne
		@JoinColumn(name = "ingredient_id")
		private Ingredient ingredient;
	}
