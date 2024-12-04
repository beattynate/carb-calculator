package carb.calculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import carb.calculator.entity.MealRecord;

public interface MealRecordDao extends JpaRepository<MealRecord, Long> {

}
