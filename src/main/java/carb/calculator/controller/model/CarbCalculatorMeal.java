package carb.calculator.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import carb.calculator.entity.Meal;
import carb.calculator.entity.MealIngredient;
import carb.calculator.entity.MealRecord;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class CarbCalculatorMeal {
	private Long mealId;
	private Long containerId;
	
	private String mealName;
	private BigDecimal totalWeight;
	private BigDecimal totalCarbGrams;
	private BigDecimal carbsPer100;
	 
	
	private Set<IngredientPortion> ingredients = new HashSet<>();
	private Set<CarbCalculatorMealRecord> mealRecords = new HashSet<>();
		
		@Data
		@NoArgsConstructor
		public static class IngredientPortion {
			Long ingredientId;
			String ingredientName;
			BigDecimal ingredientGrams;
		}	
		public void addIngredientsToMeal(Meal meal) {
				
			
			 for(MealIngredient ingredient : meal.getMealIngredients()) {
				  IngredientPortion ingredientPortion = new IngredientPortion();
				  ingredientPortion.ingredientId = ingredient.getIngredient().getIngredientId();
				  ingredientPortion.ingredientName = ingredient.getIngredient().getIngredientName();
				  ingredientPortion.ingredientGrams = ingredient.getIngredientGrams();
				  
				  this.ingredients.add(ingredientPortion);
			  }
		  }
		
	
	public CarbCalculatorMeal(Meal meal) {
		this.mealId = meal.getMealId();
		this.mealName = meal.getMealName();
		this.totalWeight = meal.getTotalWeight();
		this.totalCarbGrams = meal.getTotalCarbGrams();
		this.carbsPer100 = meal.getCarbsPer100();
		this.containerId = meal.getContainer() != null ? meal.getContainer().getContainerId() : null;
		
		addIngredientsToMeal(meal);
		
	  for(MealRecord mealRecord : meal.getMealRecords()) {
		  this.mealRecords.add(new CarbCalculatorMealRecord(mealRecord));
	  }
		
	
	  }
	

}
