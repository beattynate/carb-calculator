package carb.calculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import carb.calculator.entity.MealIngredient;

public interface MealIngredientDao extends JpaRepository<MealIngredient, Long> {

}
