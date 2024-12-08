package carb.calculator.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import carb.calculator.entity.Ingredient;
import carb.calculator.entity.MealRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientData {
	private Long ingredientId;
	
	private String ingredientName;
	private BigDecimal carbsPer100;
	
		private Set<MealRecordData> mealRecords = new HashSet<>();
	
	public IngredientData (Ingredient ingredient) {
		this.ingredientId = ingredient.getIngredientId();
		this.ingredientName = ingredient.getIngredientName();
		this.carbsPer100 = ingredient.getCarbsPer100();
		
	  for(MealRecord mealRecord : ingredient.getMealRecords()) {
		  this.mealRecords.add(new MealRecordData(mealRecord));
		   }
	  
	}
}
