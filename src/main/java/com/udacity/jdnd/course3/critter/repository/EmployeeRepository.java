package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.data.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Custom query to find employees by availability
    @Query("SELECT e FROM Employee e WHERE :dayOfWeek MEMBER OF e.daysAvailable")
    List<Employee> findByDaysAvailable(DayOfWeek dayOfWeek);
}
