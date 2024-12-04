package carb.calculator.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import carb.calculator.entity.Container;
import carb.calculator.entity.Meal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarbCalculatorContainer {
	private Long containerId;
	
	private String containerName;
	private BigDecimal containerWeight;
	
	private Set<CarbCalculatorMeal>	meals = new HashSet<>();
	
	public	CarbCalculatorContainer (Container container) {
	      this.containerId = container.getContainerId();
	      this.containerName = container.getContainerName();
	      this.containerWeight = container.getContainerWeight();
	      
	for (Meal meal : container.getMeals()) {
		this.meals.add(new CarbCalculatorMeal(meal));
	}
	}
}
