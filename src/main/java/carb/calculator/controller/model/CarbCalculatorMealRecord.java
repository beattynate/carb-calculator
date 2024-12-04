package carb.calculator.controller.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import carb.calculator.entity.Ingredient;
import carb.calculator.entity.Meal;
import carb.calculator.entity.MealRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarbCalculatorMealRecord {
	private Long mealRecordId;
	private BigDecimal portionWeight;
	private BigDecimal portionCarbs;
	private BigDecimal mealTotalWeight;
	private BigDecimal mealTotalCarbs;
	private LocalDateTime timestamp;
	
	private Set<CarbCalculatorMeal> meals = new HashSet<>();
	private Set<CarbCalculatorIngredient> ingredients = new HashSet<>();
	
	public CarbCalculatorMealRecord (MealRecord mealRecord) {
		  this.mealRecordId = mealRecord.getMealRecordId();
		  this.portionWeight = mealRecord.getPortionWeight();
		  this.portionCarbs = mealRecord.getPortionCarbs();
		  this.mealTotalWeight = mealRecord.getMealTotalWeight();
		  this.mealTotalCarbs = mealRecord.getMealTotalCarbs();
		  this.timestamp = mealRecord.getTimestamp();
		
	 for(Meal meal : mealRecord.getMeals()) {
		this.meals.add(new CarbCalculatorMeal(meal));
		}
	 for(Ingredient ingredient : mealRecord.getIngredients()) {
		this.ingredients.add(new CarbCalculatorIngredient(ingredient));
		}
	}
		
}
