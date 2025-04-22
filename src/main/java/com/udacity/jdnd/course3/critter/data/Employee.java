package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(targetClass = EmployeeSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "skill")
    private Set<EmployeeSkill> skillSet;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_availability", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "day_available")
    private Set<DayOfWeek> daysAvailable;  // Changed List to Set

    @ManyToMany(mappedBy = "employees")
    List<Schedule> schedules;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return skillSet;
    }

    public void setSkills(Set<EmployeeSkill> skillSet) {
        this.skillSet = skillSet;
    }

    public Set<DayOfWeek> getDaysAvailable() {  // Adjusted getter
        return daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> availableDays) {  // Adjusted setter
        this.daysAvailable = availableDays;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
