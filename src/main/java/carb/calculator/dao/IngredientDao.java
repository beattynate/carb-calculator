package carb.calculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import carb.calculator.entity.Ingredient;
public interface IngredientDao extends JpaRepository<Ingredient, Long> {

}
