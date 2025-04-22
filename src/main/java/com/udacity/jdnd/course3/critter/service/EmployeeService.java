package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    public Employee setEmployeeAvailability(Set<DayOfWeek> days, Long employeeId) {
        // Fetch the employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update the availability
        employee.setDaysAvailable(days);

        // Save the updated employee and return it
        return employeeRepository.save(employee);
    }


    public List<Employee> getEmployeesByService(LocalDate date, Set<EmployeeSkill> skills) {
        DayOfWeek requestedDay = date.getDayOfWeek();
        return employeeRepository.findByDaysAvailable(requestedDay)
                .stream()
                .filter(emp -> emp.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }


    public List<Employee> getEmployeesBySkillAndDay(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek){
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(dayOfWeek);
        List<Employee> employees = employeeRepository.findAll();

        List<Employee> employeeList = new ArrayList<>();
        employees.forEach(thisEmployee ->{
            if(thisEmployee.getSkills().containsAll(skills) && thisEmployee.getDaysAvailable().containsAll(dayOfWeeks)){
                employeeList.add(thisEmployee);
            }
        });
        return employeeList;
    }

}
