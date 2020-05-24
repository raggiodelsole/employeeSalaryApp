package radoslaw.kurowski.employee.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radoslaw.kurowski.employee.model.Salary;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SalaryHelperTest {

    private Optional<List<Salary>> salariesOpt;

    private SalaryHelper salaryHelper;
    private Date salaryDate1;
    private Date salaryDate2;
    private Date salaryDate3;
    private Date periodStart;
    private Date periodEnd;

    @BeforeEach
    void setUp() {
        initializeTestDates();
        initializeOptWithListOfSalaries();
        salaryHelper = new SalaryHelper();

    }

    private void initializeOptWithListOfSalaries() {
        List<Salary> testSalaries = new ArrayList<>();
        Salary mockSalary1 = mock(Salary.class);
        Salary mockSalary2 = mock(Salary.class);
        Salary mockSalary3 = mock(Salary.class);
        when(mockSalary1.getSalaryValue()).thenReturn(8500d);
        when(mockSalary2.getSalaryValue()).thenReturn(9000d);
        when(mockSalary3.getSalaryValue()).thenReturn(8000d);
        when(mockSalary1.getSalaryDate()).thenReturn(salaryDate1);
        when(mockSalary2.getSalaryDate()).thenReturn(salaryDate2);
        when(mockSalary3.getSalaryDate()).thenReturn(salaryDate3);
        testSalaries.add(mockSalary1);
        testSalaries.add(mockSalary2);
        testSalaries.add(mockSalary3);
        salariesOpt = Optional.of(testSalaries);
    }

    private void initializeTestDates() {
        Calendar calendar = Calendar.getInstance();
        salaryDate1 = calendar.getTime();
        calendar.set(2020, 05, 11);
        salaryDate2 = calendar.getTime();
        calendar.set(2019, 10, 16);
        salaryDate3 = calendar.getTime();
        calendar.set(2020, 1, 1);
        periodStart = calendar.getTime();
        calendar.set(2020, 06, 01);
        periodEnd = calendar.getTime();

    }


    @Test
    void testLastYearAvgSalaryWithCorrectValues() {
        double lastYearAvgSalary = salaryHelper.getLastYearAvgSalary(salariesOpt);
        assertEquals(lastYearAvgSalary, 8500d, 0);
    }

    @Test
    void testLastYearAvgSalaryWithEmptySalaryList() {
        double lastYearAvgSalary = salaryHelper.getLastYearAvgSalary(Optional.of(new ArrayList<>()));
        assertEquals(lastYearAvgSalary, 0d, 0);
    }

    @Test
    void testGetAvgSalaryForPeriod() {
        List<Salary> testSalaries = salariesOpt.get();
        double avgSalary = salaryHelper.getAvgSalaryForPeriod(periodStart, periodEnd, testSalaries);
        assertEquals(avgSalary, 8750, 0);
    }

    @Test
    void testGetAvgSalaryForWrongPeriod() {
        List<Salary> testSalaries = salariesOpt.get();
        double avgSalary = salaryHelper.getAvgSalaryForPeriod(periodEnd, periodEnd, testSalaries);
        assertEquals(avgSalary, 0, 0);
    }

    @Test
    void testGetAvgSalaryForPeriodUsingEmptySalaryList() {
        List<Salary> testSalaries = new ArrayList<>();
        double avgSalary = salaryHelper.getAvgSalaryForPeriod(periodStart, periodEnd, testSalaries);
        assertEquals(avgSalary, 0, 0);
    }

    @Test
    void testGetAvgSalaryForPeriodWrongPeriodValue() {
        List<Salary> testSalaries = new ArrayList<>();
        assertThrows(IllegalArgumentException.class,
                () -> salaryHelper.getAvgSalaryForPeriod(null, periodEnd, testSalaries));
    }

}