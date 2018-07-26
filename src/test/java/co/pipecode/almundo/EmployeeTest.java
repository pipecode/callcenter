package co.pipecode.almundo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import co.pipecode.almundo.domain.Call;
import co.pipecode.almundo.domain.Employee;
import co.pipecode.almundo.domain.EmployeeStatus;
import co.pipecode.almundo.domain.EmployeeType;

public class EmployeeTest {

	@Test(expected = IllegalArgumentException.class)
	public void testEmployeeInvalidCreation() {
		new Employee(null);
	}

	@Test
	public void testEmployeeOperatorCreation() {
		Employee employee = Employee.buildOperator();

		assertNotNull(employee);
		assertEquals(EmployeeType.OPERATOR, employee.getEmployeeType());
		assertEquals(EmployeeStatus.AVAILABLE, employee.getEmployeeState());
	}

	@Test
	public void testEmployeeSupervisorCreation() {
		Employee employee = Employee.buildSupervisor();

		assertNotNull(employee);
		assertEquals(EmployeeType.SUPERVISOR, employee.getEmployeeType());
		assertEquals(EmployeeStatus.AVAILABLE, employee.getEmployeeState());
	}

	@Test
	public void testEmployeeDirectorCreation() {
		Employee employee = Employee.buildDirector();

		assertNotNull(employee);
		assertEquals(EmployeeType.DIRECTOR, employee.getEmployeeType());
		assertEquals(EmployeeStatus.AVAILABLE, employee.getEmployeeState());
	}

	@Test
	public void testEmployeeStatesWhileAttend() throws InterruptedException {
		Employee employee = Employee.buildOperator();
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(employee);
		assertEquals(EmployeeStatus.AVAILABLE, employee.getEmployeeState());
		TimeUnit.SECONDS.sleep(1);
		employee.attend(new Call(1, 5, 10).buildRandomCall());
		employee.attend(new Call(1, 5, 10).buildRandomCall());
		TimeUnit.SECONDS.sleep(1);
		assertEquals(EmployeeStatus.BUSY, employee.getEmployeeState());

		executorService.awaitTermination(30, TimeUnit.SECONDS);
		assertEquals(2, employee.getAttendedCalls().size());
	}

	@Test
	public void testEmployeeAttendWhileAvailable() throws InterruptedException {
		Employee employee = Employee.buildOperator();
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(employee);
		employee.attend(new Call(1, 5, 10).buildRandomCall());

		assertEquals(EmployeeStatus.AVAILABLE, employee.getEmployeeState());
		executorService.awaitTermination(20, TimeUnit.SECONDS);
		assertEquals(1, employee.getAttendedCalls().size());
	}

}
