// ScheduleRepository.java
package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.data.*;
import com.udacity.jdnd.course3.critter.data.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByPets(Pet pet);
    List<Schedule> findByEmployees(Employee employee);
    List<Schedule> findByPetsIn(List<Pet> pets);
}
