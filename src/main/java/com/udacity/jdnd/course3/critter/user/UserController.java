package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.*;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    @Transactional
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            // Log the incoming request
            logger.info("Saving customer: {}", customerDTO);

            // Convert DTO to entity
            Customer customer = new Customer();
            BeanUtils.copyProperties(customerDTO, customer);

            // Validate petIds before processing
            List<Long> petIds = customerDTO.getPetIds();
            if (petIds == null || petIds.isEmpty()) {
                logger.warn("No pets associated with the customer");
            }

            // Save customer and associate pets
            Customer savedCustomer = customerService.saveCustomer(customer, petIds);

            // Log the success
            logger.info("Customer saved successfully with ID: {}", savedCustomer.getId());

            return convertCustomerToCustomerDTO(savedCustomer);
        } catch (Exception e) {
            logger.error("Error saving customer", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving customer", e);
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return customers.stream()
                    .map(this::convertCustomerToCustomerDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all customers", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all customers", e);
        }
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        try {
            Customer customer = customerService.getCustomerByPetId(petId);
            return convertCustomerToCustomerDTO(customer);
        } catch (Exception exception) {
            logger.error("Owner not found for pet ID: {}", petId, exception);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner pet with id: " + petId + " not found", exception);
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeeDTO, employee);

            Employee savedEmployee = employeeService.saveEmployee(employee);
            return convertEmployeeToEmployeeDTO(savedEmployee);
        } catch (Exception e) {
            logger.error("Error saving employee", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving employee", e);
        }
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            return convertEmployeeToEmployeeDTO(employee);
        } catch (Exception e) {
            logger.error("Error fetching employee with ID: {}", employeeId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching employee", e);
        }
    }

    @PutMapping("/employee/{employeeId}")
    public ResponseEntity<Employee> setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            // Call the service method to update availability
            Employee updatedEmployee = employeeService.setEmployeeAvailability(daysAvailable, employeeId);

            // Return the updated employee with a 200 OK status
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            // Log the error and throw a 500 Internal Server Error if something goes wrong
            logger.error("Error setting availability for employee ID: {}", employeeId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error setting availability", e);
        }
    }


    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        try {
            Set<EmployeeSkill> requiredSkills = employeeRequestDTO.getSkills();
            DayOfWeek day = employeeRequestDTO.getDate().getDayOfWeek();

            List<Employee> matchingEmployees = employeeService.getEmployeesBySkillAndDay(requiredSkills, day);
            return matchingEmployees.stream()
                    .map(this::convertEmployeeToEmployeeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding employees for service", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error finding employees for service", e);
        }
    }

    // Utility methods to convert entities to DTOs
    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);

        List<Long> petIds = new ArrayList<>();
        if (customer.getPets() != null) {
            for (Pet pet : customer.getPets()) {
                petIds.add(pet.getId());
            }
        }
        dto.setPetIds(petIds);
        return dto;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }


}
