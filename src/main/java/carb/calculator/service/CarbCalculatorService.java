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

import carb.calculator.controller.model.IngredientData;
import carb.calculator.controller.model.MealData;
import carb.calculator.controller.model.MealRecordData;
import carb.calculator.controller.model.ContainerData;
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

	@Transactional(readOnly = false)
	public MealRecordData saveMealRecord(MealRecordData mealRecordData) {
		Long mealRecordId = mealRecordData.getMealRecordId();
		MealRecord mealRecord = findOrCreateMealRecord(mealRecordId);
		
		mealRecord.setTimestamp(LocalDateTime.now());
		
		copyMealRecordFields(mealRecord, mealRecordData);
		return new MealRecordData(mealRecordDao.save(mealRecord));
		
	}

	private void copyMealRecordFields(MealRecord mealRecord, MealRecordData mealRecordData) {
		mealRecord.setPortionWeight(mealRecordData.getPortionWeight());
		mealRecord.setPortionCarbs(mealRecordData.getPortionCarbs());
		mealRecord.setMealTotalWeight(mealRecordData.getMealTotalWeight());
		mealRecord.setMealTotalCarbs(mealRecordData.getMealTotalCarbs());
		mealRecord.setTimestamp(mealRecordData.getTimestamp());
		
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

	@Transactional(readOnly = false)
	public IngredientData saveIngredient(IngredientData ingredientData) {
		Long ingredientId = ingredientData.getIngredientId();
		Ingredient ingredient = findOrCreateIngredient(ingredientId);
		
		if (Objects.isNull(ingredientData.getCarbsPer100()) || ingredientData.getCarbsPer100().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Carbs per 100 grams must be greater than 0.");
        }
		
		copyIngredientFields(ingredient, ingredientData);
		return new IngredientData(ingredientDao.save(ingredient));
		
	}

	private void copyIngredientFields(Ingredient ingredient, IngredientData ingredientData) {
		ingredient.setIngredientName(ingredientData.getIngredientName());
		ingredient.setCarbsPer100(ingredientData.getCarbsPer100());
		
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

	@Transactional(readOnly = false)
	public ContainerData saveContainer(ContainerData containerData) {
		Long containerId = containerData.getContainerId();
		Container container = findOrCreateContainer(containerId);

		copyContainerFields(container, containerData);
		return new ContainerData(containerDao.save(container));
	}

	private void copyContainerFields(Container container, ContainerData containerData) {
		container.setContainerName(containerData.getContainerName());
		container.setContainerWeight(containerData.getContainerWeight());
				
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

	@Transactional(readOnly = false)
	public MealData saveMeal(MealData mealData) {
		
		Long mealId = mealData.getMealId();
		Meal meal = findOrCreateMeal(mealId);
		Container container = findContainerById(mealData.getContainerId());
		BigDecimal carbs = mealData.getTotalCarbGrams();
		BigDecimal mealWeight = mealData.getTotalWeight();
		
		 if (mealWeight == null || mealWeight.compareTo(BigDecimal.ZERO) <= 0) {
		        throw new IllegalArgumentException("Meal weight must be greater than zero.");
		    }
		BigDecimal containerWeight = container.getContainerWeight();
		BigDecimal netWeight = mealWeight.subtract(containerWeight);
		
		if (netWeight.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("Net weight must be greater than zero.");
	    }
		
		BigDecimal carbsPer100 = carbs.multiply(BigDecimal.valueOf(100))
						.divide(netWeight, RoundingMode.HALF_UP);
				
		mealData.setCarbsPer100(carbsPer100);
		
		
		
		copyMealFields(meal, mealData);
		return new MealData(mealDao.save(meal));
	}

	private void copyMealFields(Meal meal, MealData mealData) {
		Long containerId = mealData.getContainerId();
		
		meal.setMealName(mealData.getMealName());
		meal.setTotalWeight(mealData.getTotalWeight());
		meal.setTotalCarbGrams(mealData.getTotalCarbGrams());
		meal.setCarbsPer100(mealData.getCarbsPer100());
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
	public List<MealRecordData> retrieveAllMealRecords() {
		
		List<MealRecord> mealRecords = new LinkedList<>(mealRecordDao.findAll());
		List<MealRecordData> result = new LinkedList<>();
		
		for(MealRecord mealRecord : mealRecords) {
			MealRecordData mrd = new MealRecordData(mealRecord);
			
			result.add(mrd);
		}
		return result;
	}
	
	@Transactional (readOnly = true)
	public MealRecordData retrieveMealRecord(Long mealRecordId) {
		MealRecord mealRecord = findMealRecordById(mealRecordId);
		MealRecordData mrd = new MealRecordData(mealRecord);
		return mrd;
	}
    
	@Transactional (readOnly = false)
	public void deleteMealRecordById(Long mealRecordId) {
		MealRecord mealRecord = findMealRecordById(mealRecordId);
		mealRecordDao.delete(mealRecord);
		
	}
	
	@Transactional (readOnly = false)
	public MealData addIngredientToMeal(Long ingredientId, Long mealId, BigDecimal ingredientGrams) {
		Meal meal = findMealById(mealId);
		Ingredient ingredient = findIngredientById(ingredientId);
		MealIngredient mealIngredient = new MealIngredient();
		BigDecimal ingredientCarbsPer100 = ingredient.getCarbsPer100();
		
		if (Objects.isNull(ingredientGrams) || ingredientGrams.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Ingredient grams must be greater than 0");
		}
		
		BigDecimal portionCarbs = ingredientCarbsPer100.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).
				multiply(ingredientGrams);
		BigDecimal mealCarbs = meal.getTotalCarbGrams();
		BigDecimal updatedTotalCarbs = mealCarbs.add(portionCarbs);
		
		
		meal.setTotalCarbGrams(updatedTotalCarbs);
		
		mealIngredient.setIngredient(ingredient);
		mealIngredient.setMeal(meal);
		mealIngredient.setIngredientGrams(ingredientGrams);
		mealIngredientDao.save(mealIngredient);
				
		return saveMeal(new MealData(meal));
	}
	
	

	
}
