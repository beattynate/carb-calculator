package carb.calculator.dao;

import carb.calculator.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerDao extends JpaRepository<Container, Long> {

}
