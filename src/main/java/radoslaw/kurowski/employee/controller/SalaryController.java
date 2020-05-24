package radoslaw.kurowski.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import radoslaw.kurowski.employee.exception.ResourceNotFoundException;
import radoslaw.kurowski.employee.helper.SalaryHelper;
import radoslaw.kurowski.employee.model.Employee;
import radoslaw.kurowski.employee.model.Salary;
import radoslaw.kurowski.employee.model.SalaryPeriod;
import radoslaw.kurowski.employee.repository.EmployeeRepository;
import radoslaw.kurowski.employee.repository.SalaryRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RestController
public class SalaryController {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryHelper salaryHelper;

    /**
     * This method returns all salaries from salary table
     *
     * @return List of salaries objects
     */
    @GetMapping("/getAllSalaries")
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    /**
     * Return all salaries that are assign to employee with id passed as aprameter
     *
     * @param employeeId used to identify employee in db
     * @return list of salaries
     */
    @GetMapping("/employee/{employeeId}/getSalaries")
    public List<Salary> getSalaryByEmployeeId(@PathVariable Long employeeId) {
        return ofNullable(salaryRepository.findByEmployeeId(employeeId))
                .orElse(Collections.emptyList());
    }


    /**
     * Adding salary to Salary table in database
     *
     * @param employeeId Employee that salary is assign to
     * @param salary     object to add into salary table in database
     * @return Added Salary if object was successfully added to db
     */
    @PostMapping("/employee/{employeeId}/addSalary")
    public Salary addSalary(@PathVariable Long employeeId,
                            @Valid @RequestBody Salary salary) {
        return employeeRepository.findById(employeeId)
                .map(employee -> {
                    salary.setEmployee(employee);
                    return salaryRepository.save(salary);
                }).orElseThrow(() -> new ResourceNotFoundException("Salary with id = " + salary.getId() + "not found"));
    }

    @PutMapping("/employee/{employeeId}/updateSalary/{salaryId}")
    public Salary updateSalary(@PathVariable Long employeeId,
                               @PathVariable Long salaryId,
                               @Valid @RequestBody Salary salaryRequest) {
        Employee employee;
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " not found");
        } else {
            employee = employeeRepository.findById(employeeId).get();
        }
        return salaryRepository.findById(salaryId)
                .map(salary -> {
                            salary.setId(salaryId);
                            salary.setEmployee(employee);
                            salary.setSalaryDate(salaryRequest.getSalaryDate());
                            salary.setSalaryText(salaryRequest.getSalaryText());
                            salary.setSalaryValue(salaryRequest.getSalaryValue());
                            salary.setSalaryType(salaryRequest.getSalaryType());
                            return salaryRepository.save(salary);
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("Salary with id " + salaryId + " not found"));
    }

    @DeleteMapping("salaries/{salaryId}")
    public ResponseEntity<?> deleteSalary(@PathVariable Long employeeId,
                                          @PathVariable Long salaryId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + "not found");
        }
        return salaryRepository.findById(salaryId)
                .map(salary -> {
                    salaryRepository.delete(salary);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Salary with id " + salaryId + "not found"));
    }

    @GetMapping("getAvgSalaries/{employeeId}")
    public double lastYearAvgSalary(@PathVariable Long employeeId) {
        return salaryHelper.getLastYearAvgSalary(Optional.ofNullable(salaryRepository.findByEmployeeId(employeeId)));
    }

    @PostMapping("getPeriodAvgSalaries/{employeeId}")
    public double getPeriodAvgSalary(@PathVariable Long employeeId,
                                     @Valid @RequestBody SalaryPeriod period) {
        return salaryHelper.getAvgSalaryForPeriod(period.getPeriodStart(), period.getPeriodEnd(), salaryRepository.findByEmployeeId(employeeId));
    }


}
