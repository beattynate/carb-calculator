package carb.calculator.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import carb.calculator.controller.model.CarbCalculatorContainer;
import carb.calculator.controller.model.CarbCalculatorIngredient;
import carb.calculator.controller.model.CarbCalculatorMeal;
import carb.calculator.controller.model.CarbCalculatorMealRecord;
import carb.calculator.service.CarbCalculatorService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/carb_calculator")
@Slf4j
public class CarbCalculatorController {
	@Autowired
	private CarbCalculatorService carbCalculatorService;
	
	@PostMapping("/meal_record")
	public CarbCalculatorMealRecord insertMealRecord(
			@RequestBody CarbCalculatorMealRecord carbCalculatorMealRecord) {
		log.info("Creating meal record {}", carbCalculatorMealRecord);
		return carbCalculatorService.saveMealRecord(carbCalculatorMealRecord);
}
	@PutMapping("/meal_record/{mealRecordId}")
	public CarbCalculatorMealRecord modifyMealRecord(@PathVariable Long mealRecordId,
			@RequestBody CarbCalculatorMealRecord carbCalculatorMealRecord) {
		carbCalculatorMealRecord.setMealRecordId(mealRecordId);
		log.info("Updating meal record {}", mealRecordId);
		return carbCalculatorService.saveMealRecord(carbCalculatorMealRecord);
		}
	@PostMapping("/ingredient")
	public CarbCalculatorIngredient insertIngredient(
			@RequestBody CarbCalculatorIngredient carbCalculatorIngredient) {
		log.info("Creating ingredient {}", carbCalculatorIngredient);
		return carbCalculatorService.saveIngredient(carbCalculatorIngredient);
	}
	@PostMapping("/container")
	public CarbCalculatorContainer insertContainer(
			@RequestBody CarbCalculatorContainer carbCalculatorContainer) {
		log.info("Creating container {}", carbCalculatorContainer);
		return carbCalculatorService.saveContainer(carbCalculatorContainer);
	}
	@PostMapping("/meal")
	public CarbCalculatorMeal insertMeal(
			@RequestBody CarbCalculatorMeal carbCalculatorMeal) {
		log.info("Creating meal {}", carbCalculatorMeal);
		return carbCalculatorService.saveMeal(carbCalculatorMeal);
	}
	@GetMapping("/meal_records")
	public List<CarbCalculatorMealRecord> retrieveMealRecords(){
		return carbCalculatorService.retrieveAllMealRecords();
	}
	@GetMapping("/meal_record/{mealRecordId}")
	public CarbCalculatorMealRecord retrieveMealRecordById(@PathVariable Long mealRecordId) {
		return carbCalculatorService.retrieveMealRecord(mealRecordId);
	}
	@DeleteMapping("/meal_record/{mealRecordId}")
	public Map<String, String> deleteMealRecordById(
			@PathVariable Long mealRecordId) {
		log.info("Deleting meal record with ID= {}", mealRecordId);
		
		carbCalculatorService.deleteMealRecordById(mealRecordId);
		
		return Map.of("message", "Deletion of meal record with ID=" + mealRecordId + " was succesful.");
	}
	@PostMapping("/meal/{mealId}/ingredient")
	public CarbCalculatorMeal addIngredientToMeal(@PathVariable Long mealId,
			@RequestBody CarbCalculatorMeal.IngredientPortion ingredientPortion) {
		Long ingredientId = ingredientPortion.getIngredientId();
		BigDecimal ingredientGrams = ingredientPortion.getIngredientGrams();
		
		log.info("Adding ingredient to meal {}", ingredientPortion);
		
		return carbCalculatorService.addIngredientToMeal(ingredientId, mealId, ingredientGrams);
	}
}