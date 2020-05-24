package radoslaw.kurowski.employee.helper;

import org.springframework.stereotype.Component;
import radoslaw.kurowski.employee.model.Salary;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SalaryHelper {


    /**
     * This method calculates avg salary from last 12 months from optional containing List of salaries
     *
     * @param salaryOpt optional containing List of Salaries
     * @return double value of avg Salary or 0 as default
     */
    public double getLastYearAvgSalary(Optional<List<Salary>> salaryOpt) {
        List<Salary> salaries = getLastYearSalaries(salaryOpt);
        return getAvgSalary(salaries);
    }

    /**
     * This method calculates avg salary in period between 2 period parameters
     *
     * @param periodStart begging of period
     * @param periodEnd   end of period
     * @param allSalary   list of all salaries that might be used
     * @return double value of avg salary or IllegalArgumentException() if period param is null
     */
    public double getAvgSalaryForPeriod(Date periodStart, Date periodEnd, List<Salary> allSalary) {
        boolean periodEqualNull = periodEnd == null || periodStart == null;
        if (periodEqualNull) {
            throw new IllegalArgumentException();
        }
        List<Salary> salaries = getSalariesFromPeriod(periodStart, periodEnd, allSalary);
        return getAvgSalary(salaries);
    }

    private List<Salary> getLastYearSalaries(Optional<List<Salary>> salaryOpt) {
        List<Salary> salaries = new ArrayList<>();
        Date theEarliestDate = getEarliestDate();

        if (salaryOpt.isPresent()) {
            salaries.addAll(getSalariesWthDateAfter(salaryOpt, theEarliestDate));
        }
        return salaries;

    }

    private List<Salary> getSalariesWthDateAfter(Optional<List<Salary>> salaryOpt, Date theEarliestDate) {
        return salaryOpt.get().stream()
                .filter(x -> x.getSalaryDate().after(theEarliestDate))
                .collect(Collectors.toList());
    }

    private Date getEarliestDate() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate theEarliestDate = LocalDate.now().minusMonths(12);
        return Date.from(theEarliestDate.atStartOfDay(defaultZoneId).toInstant());
    }

    private double getAvgSalary(List<Salary> salaries) {
        double avgSalary = 0;
        for (Salary salary : salaries) {
            avgSalary += salary.getSalaryValue();
        }
        if (salaries.size() != 0) {
            avgSalary /= salaries.size();
        } else {
            avgSalary = 0;
        }
        return Math.round(avgSalary * 100.0) / 100.0;
    }

    private List<Salary> getSalariesFromPeriod(Date periodStart, Date periodEnd, List<Salary> allSalary) {
        return allSalary.stream()
                .filter(s -> s.getSalaryDate().after(periodStart))
                .filter(s -> s.getSalaryDate().before(periodEnd))
                .collect(Collectors.toList());
    }

}
