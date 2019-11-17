package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.springframework.data.repository.*;
import java.util.*;

public interface MyDatabaseConnection extends CrudRepository<Circle, Integer> {
    List<Circle> findByRadiusOrderByIdDesc(double radius);
}