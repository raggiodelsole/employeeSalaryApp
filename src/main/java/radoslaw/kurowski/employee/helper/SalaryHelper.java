package radoslaw.kurowski.employee.helper;

import radoslaw.kurowski.employee.model.Salary;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalaryHelper {
    private List<Salary> getLastYearSalaries(Optional<List<Salary>> salaryOpt) {
        List<Salary> salaries = Collections.emptyList();
        Date theEarliestDate = getEarliestDate();

        if (salaryOpt.isPresent()) {
            salaries.addAll(salaries.stream()
                    .filter(x -> x.getSalaryDate().after(theEarliestDate))
                    .collect(Collectors.toList()));
        }
        return salaries;

    }

    private Date getEarliestDate() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate theEarliestDate = LocalDate.now().minusYears(1);
        return Date.from(theEarliestDate.atStartOfDay(defaultZoneId).toInstant());
    }

    public double getAvgSalary(List<Salary> salaries) {
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
    public double getLastYearAvgSalary(Optional<List<Salary>> salaryOpt){
        List<Salary> salaries = getLastYearSalaries(salaryOpt);
        return getAvgSalary(salaries);
    }

    public double getAvgSalaryForPeriod(Date periodStart, Date periodEnd, List<Salary> allSalary){
        List<Salary> salaries = getSalariesFromPeriod( periodStart,  periodEnd, allSalary);
        return getAvgSalary(salaries);
    }

    private List<Salary> getSalariesFromPeriod(Date periodStart, Date periodEnd, List<Salary> allSalary) {
        return allSalary.stream()
                .filter(s->s.getSalaryDate().after(periodStart))
                .filter(s->s.getSalaryDate().before(periodEnd))
                .collect(Collectors.toList());
    }

}
