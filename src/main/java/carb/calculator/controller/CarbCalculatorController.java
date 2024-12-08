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

import carb.calculator.controller.model.ContainerData;
import carb.calculator.controller.model.IngredientData;
import carb.calculator.controller.model.MealData;
import carb.calculator.controller.model.MealRecordData;
import carb.calculator.service.CarbCalculatorService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/carb_calculator")
@Slf4j
public class CarbCalculatorController {
	@Autowired
	private CarbCalculatorService carbCalculatorService;
	
	@PostMapping("/meal_record")
	public MealRecordData insertMealRecord(
			@RequestBody MealRecordData mealRecordData) {
		log.info("Creating meal record {}", mealRecordData);
		return carbCalculatorService.saveMealRecord(mealRecordData);
}
	@PutMapping("/meal_record/{mealRecordId}")
	public MealRecordData modifyMealRecord(@PathVariable Long mealRecordId,
			@RequestBody MealRecordData mealRecordData) {
		mealRecordData.setMealRecordId(mealRecordId);
		log.info("Updating meal record {}", mealRecordId);
		return carbCalculatorService.saveMealRecord(mealRecordData);
		}
	@PostMapping("/ingredient")
	public IngredientData insertIngredient(
			@RequestBody IngredientData ingredientData) {
		log.info("Creating ingredient {}", ingredientData);
		return carbCalculatorService.saveIngredient(ingredientData);
	}
	@PostMapping("/container")
	public ContainerData insertContainer(
			@RequestBody ContainerData containerData) {
		log.info("Creating container {}", containerData);
		return carbCalculatorService.saveContainer(containerData);
	}
	@PostMapping("/meal")
	public MealData insertMeal(
			@RequestBody MealData mealData) {
		log.info("Creating meal {}", mealData);
		return carbCalculatorService.saveMeal(mealData);
	}
	@GetMapping("/meal_records")
	public List<MealRecordData> retrieveMealRecords(){
		return carbCalculatorService.retrieveAllMealRecords();
	}
	@GetMapping("/meal_record/{mealRecordId}")
	public MealRecordData retrieveMealRecordById(@PathVariable Long mealRecordId) {
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
	public MealData addIngredientToMeal(@PathVariable Long mealId,
			@RequestBody MealData.IngredientPortion ingredientPortion) {
		Long ingredientId = ingredientPortion.getIngredientId();
		BigDecimal ingredientGrams = ingredientPortion.getIngredientGrams();
		
		log.info("Adding ingredient to meal {}", ingredientPortion);
		
		return carbCalculatorService.addIngredientToMeal(ingredientId, mealId, ingredientGrams);
	}
}