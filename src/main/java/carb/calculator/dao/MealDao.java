package carb.calculator.dao;

import carb.calculator.entity.Meal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDao extends JpaRepository<Meal, Long> {

}
