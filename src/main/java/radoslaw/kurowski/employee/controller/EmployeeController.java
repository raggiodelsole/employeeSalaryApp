package radoslaw.kurowski.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import radoslaw.kurowski.employee.exception.ResourceNotFoundException;
import radoslaw.kurowski.employee.model.Employee;
import radoslaw.kurowski.employee.repository.EmployeeRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This method return Employee object from db
     *
     * @param employeeId EmployeeId
     * @return Employee object
     */
    @GetMapping("/getEmployee/{employeeId}")
    public Employee getEmployee(@PathVariable long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + employeeId + "not found in database."));

    }

    /**
     * This method returns list of all Employees from Employee table.
     *
     * @return List of Employees
     */
    @GetMapping("/getEmployees")
    public List<Employee> getEmployees() {
        return Optional.ofNullable(employeeRepository.findAll()).orElse(Collections.emptyList());

    }

    /**
     * This method add Employee entity from request to Employee table in db
     *
     * @param employee Employee entity passed to db
     * @return Added Employee if object was successfully added to db
     */
    @PostMapping("/addEmployee")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * This method updates Employee with employeeId in db
     *
     * @param employeeId      updating employeeId
     * @param employeeRequest object containing new Employee field values
     * @return Updated Employee if successfully updated in db
     */
    @PutMapping("/updateEmployee/{employeeId}")
    public Employee updateEmployee(@PathVariable Long employeeId,
                                   @Valid @RequestBody Employee employeeRequest) {
        return employeeRepository.findById(employeeId)
                .map(employee -> {
                    employee.setFirstName(employeeRequest.getFirstName());
                    employee.setLastName(employeeRequest.getLastName());
                    employee.setHiredDate(employeeRequest.getHiredDate());
                    employee.setEmployeeId(employeeRequest.getEmployeeId());
                    employee.setId(employeeId);
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employeeId + " not found."));
    }

    /**
     * This method removes employee from database.
     *
     * @param employeeId employee id to be removed
     * @return HTTP status ok when employee was successfully removed
     */
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employeeId + "not found."));
    }

}
