package carb.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carb.calculator.controller.model.CarbCalculatorContainer;
import carb.calculator.controller.model.CarbCalculatorIngredient;
import carb.calculator.controller.model.CarbCalculatorMeal;
import carb.calculator.controller.model.CarbCalculatorMealRecord;
import carb.calculator.dao.ContainerDao;
import carb.calculator.dao.IngredientDao;
import carb.calculator.dao.MealDao;
import carb.calculator.dao.MealIngredientDao;
import carb.calculator.dao.MealRecordDao;
import carb.calculator.entity.Container;
import carb.calculator.entity.Ingredient;
import carb.calculator.entity.Meal;
import carb.calculator.entity.MealIngredient;
import carb.calculator.entity.MealRecord;

@Service
public class CarbCalculatorService {
	@Autowired
	private MealRecordDao mealRecordDao;
	
	@Autowired
	private MealDao mealDao;
	
	@Autowired
	private ContainerDao containerDao;
	
	@Autowired
	private IngredientDao ingredientDao;
	
	@Autowired
	private MealIngredientDao mealIngredientDao;

	public CarbCalculatorMealRecord saveMealRecord(CarbCalculatorMealRecord carbCalculatorMealRecord) {
		Long mealRecordId = carbCalculatorMealRecord.getMealRecordId();
		MealRecord mealRecord = findOrCreateMealRecord(mealRecordId);
		
		mealRecord.setTimestamp(LocalDateTime.now());
		
		copyMealRecordFields(mealRecord, carbCalculatorMealRecord);
		return new CarbCalculatorMealRecord(mealRecordDao.save(mealRecord));
		
	}

	private void copyMealRecordFields(MealRecord mealRecord, CarbCalculatorMealRecord carbCalculatorMealRecord) {
		mealRecord.setPortionWeight(carbCalculatorMealRecord.getPortionWeight());
		mealRecord.setPortionCarbs(carbCalculatorMealRecord.getPortionCarbs());
		mealRecord.setMealTotalWeight(carbCalculatorMealRecord.getMealTotalWeight());
		mealRecord.setMealTotalCarbs(carbCalculatorMealRecord.getMealTotalCarbs());
		mealRecord.setTimestamp(carbCalculatorMealRecord.getTimestamp());
		
	}

	private MealRecord findOrCreateMealRecord(Long mealRecordId) {
		if (Objects.isNull(mealRecordId)) {
			return new MealRecord();
		}else {
			return findMealRecordById(mealRecordId);
		}}
		

	private MealRecord findMealRecordById(Long mealRecordId) {
		return mealRecordDao.findById(mealRecordId)
				.orElseThrow(() -> new NoSuchElementException(
						"Meal record with ID=" + mealRecordId + " was not found."));
	}

	public CarbCalculatorIngredient saveIngredient(CarbCalculatorIngredient carbCalculatorIngredient) {
		Long ingredientId = carbCalculatorIngredient.getIngredientId();
		Ingredient ingredient = findOrCreateIngredient(ingredientId);
		
		copyIngredientFields(ingredient, carbCalculatorIngredient);
		return new CarbCalculatorIngredient(ingredientDao.save(ingredient));
		
	}

	private void copyIngredientFields(Ingredient ingredient, CarbCalculatorIngredient carbCalculatorIngredient) {
		ingredient.setIngredientName(carbCalculatorIngredient.getIngredientName());
		ingredient.setCarbsPer100(carbCalculatorIngredient.getCarbsPer100());
		
	}

	private Ingredient findOrCreateIngredient(Long ingredientId) {
		if (Objects.isNull(ingredientId)) {
			return new Ingredient();
		}else {
			return findIngredientById(ingredientId);
		}
	}

	private Ingredient findIngredientById(Long ingredientId) {
		return ingredientDao.findById(ingredientId)
				.orElseThrow(() -> new NoSuchElementException(
						"Ingredient with ID=" + ingredientId + " was not found."));
	}

	public CarbCalculatorContainer saveContainer(CarbCalculatorContainer carbCalculatorContainer) {
		Long containerId = carbCalculatorContainer.getContainerId();
		Container container = findOrCreateContainer(containerId);

		copyContainerFields(container, carbCalculatorContainer);
		return new CarbCalculatorContainer(containerDao.save(container));
	}

	private void copyContainerFields(Container container, CarbCalculatorContainer carbCalculatorContainer) {
		container.setContainerName(carbCalculatorContainer.getContainerName());
		container.setContainerWeight(carbCalculatorContainer.getContainerWeight());
				
	}

	private Container findOrCreateContainer(Long containerId) {
		if (Objects.isNull(containerId)) {
			return new Container();
		}else {
			return findContainerById(containerId);
		}
	}

	private Container findContainerById(Long containerId) {
		return containerDao.findById(containerId)
				.orElseThrow(() -> new NoSuchElementException(
						"Container with ID=" + containerId + " was not found."));
	}

	public CarbCalculatorMeal saveMeal(CarbCalculatorMeal carbCalculatorMeal) {
		//TODO create validations for null and zero values
		Long mealId = carbCalculatorMeal.getMealId();
		Meal meal = findOrCreateMeal(mealId);
		Container container = findContainerById(carbCalculatorMeal.getContainerId());
		
		BigDecimal carbs = carbCalculatorMeal.getTotalCarbGrams();
		BigDecimal mealWeight = carbCalculatorMeal.getTotalWeight();
		BigDecimal containerWeight = container.getContainerWeight();
		BigDecimal netWeight = mealWeight.subtract(containerWeight);
		BigDecimal carbsPer100 = carbs.multiply(BigDecimal.valueOf(100))
						.divide(netWeight, RoundingMode.HALF_UP);
				
		carbCalculatorMeal.setCarbsPer100(carbsPer100);
		
		
		
		copyMealFields(meal, carbCalculatorMeal);
		return new CarbCalculatorMeal(mealDao.save(meal));
	}

	private void copyMealFields(Meal meal, CarbCalculatorMeal carbCalculatorMeal) {
		Long containerId = carbCalculatorMeal.getContainerId();
		
		meal.setMealName(carbCalculatorMeal.getMealName());
		meal.setTotalWeight(carbCalculatorMeal.getTotalWeight());
		meal.setTotalCarbGrams(carbCalculatorMeal.getTotalCarbGrams());
		meal.setCarbsPer100(carbCalculatorMeal.getCarbsPer100());
		meal.setContainer(findContainerById(containerId));
		
		
	}
	
	private Meal findOrCreateMeal(Long mealId) {
		if (Objects.isNull(mealId)) {
			return new Meal();
		}else {
			return findMealById(mealId);
		}
	
	}

	private Meal findMealById(Long mealId) {
		return mealDao.findById(mealId)
				.orElseThrow(() -> new NoSuchElementException(
						"Meal with ID=" + mealId + " was not found."));
	}

	@Transactional (readOnly = true)
	public List<CarbCalculatorMealRecord> retrieveAllMealRecords() {
		
		List<MealRecord> mealRecords = new LinkedList<>(mealRecordDao.findAll());
		List<CarbCalculatorMealRecord> result = new LinkedList<>();
		
		for(MealRecord mealRecord : mealRecords) {
			CarbCalculatorMealRecord ccmr = new CarbCalculatorMealRecord();
			
			result.add(ccmr);
		}
		return result;
	}
	
	@Transactional (readOnly = true)
	public CarbCalculatorMealRecord retrieveMealRecord(Long mealRecordId) {
		MealRecord mealRecord = findMealRecordById(mealRecordId);
		CarbCalculatorMealRecord ccmr = new CarbCalculatorMealRecord(mealRecord);
		return ccmr;
	}
    
	@Transactional (readOnly = false)
	public void deleteMealRecordById(Long mealRecordId) {
		MealRecord mealRecord = findMealRecordById(mealRecordId);
		mealRecordDao.delete(mealRecord);
		
	}
	//TODO validations
	@Transactional (readOnly = false)
	public CarbCalculatorMeal addIngredientToMeal(Long ingredientId, Long mealId, BigDecimal ingredientGrams) {
		Meal meal = findMealById(mealId);
		Ingredient ingredient = findIngredientById(ingredientId);
		MealIngredient mealIngredient = new MealIngredient();
		BigDecimal ingredientCarbsPer100 = ingredient.getCarbsPer100();
		BigDecimal portionCarbs = ingredientCarbsPer100.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).
				multiply(ingredientGrams);
		BigDecimal mealCarbs = meal.getTotalCarbGrams();
		BigDecimal updatedTotalCarbs = mealCarbs.add(portionCarbs);
		
		
		meal.setTotalCarbGrams(updatedTotalCarbs);
		
		mealIngredient.setIngredient(ingredient);
		mealIngredient.setMeal(meal);
		mealIngredient.setIngredientGrams(ingredientGrams);
		mealIngredientDao.save(mealIngredient);
				
		return saveMeal(new CarbCalculatorMeal(meal));
	}
	
	

	
}
