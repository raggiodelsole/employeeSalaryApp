package radoslaw.kurowski.employee.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "emplyees")
public class Employee extends EmployeeModel {

    @Id
    @GeneratedValue(generator = "emplyee_generator")
    @SequenceGenerator(
            name = "emplyee_generator",
            sequenceName = "emplyee_sequence",
            initialValue = 1
    )
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date hiredDate;

    private String firstName;
    private String lastName;
    private Integer employeeId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }

    @Override
    public String toString() {
        return "<br>Employee id = " + id
                + "<br>Employee name: " + firstName + " " + lastName
                + "<br>Employee id: " + employeeId
                + "<br>Employee hired date: " + hiredDate + "<br><br>";

    }
}
