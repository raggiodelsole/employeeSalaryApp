package radoslaw.kurowski.employee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "salaries")
public class Salary extends EmployeeModel {

    @Id
    @GeneratedValue(generator = "salary_generator")
    @SequenceGenerator(
            name = "salary_generator",
            sequenceName = "salary_sequence",
            initialValue = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Employee employee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date salaryDate;

    private Integer salaryType;
    private String salaryText;
    private double salaryValue;


    public double getSalaryValue() {
        return salaryValue;
    }

    public void setSalaryValue(double salaryValue) {
        this.salaryValue = salaryValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "<br> Salary id: " + id
                + "<br>Salary amount: " + salaryValue
                + "<br>Salary for date: " + salaryDate
                + "<br>Salary type: " + salaryType
                + "<br>Salary type: " + salaryText
                + " <br>";
    }

    public Date getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }

    public Integer getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(Integer salaryType) {
        this.salaryType = salaryType;
    }

    public String getSalaryText() {
        return salaryText;
    }

    public void setSalaryText(String salaryText) {
        this.salaryText = salaryText;
    }
}
