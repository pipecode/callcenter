package co.pipecode.almundo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import co.pipecode.almundo.domain.Call;
import co.pipecode.almundo.domain.Employee;

public class DispatcherTest {

	@Test
	public void testDispatch10CallsToEmployees() throws InterruptedException {
		int totalCalls = 10;
		int minDuration = 3;
		int maxDuration = 6;

		List<Employee> employeeList = build10Employees();
		Dispatcher dispatcher = new Dispatcher(employeeList);
		dispatcher.start();
		TimeUnit.SECONDS.sleep(1);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(dispatcher);
		TimeUnit.SECONDS.sleep(1);

		new Call(1, minDuration, maxDuration).buildRandomCalls(totalCalls).stream().forEach(call -> {
			try {
				dispatcher.dispatchCall(call);
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				fail();
			}
		});

		executorService.awaitTermination(maxDuration * 2, TimeUnit.SECONDS);
		assertEquals(totalCalls, employeeList.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
	}

	public List<Employee> build10Employees() {
		Employee operator1 = Employee.buildOperator();
		Employee operator2 = Employee.buildOperator();
		Employee operator3 = Employee.buildOperator();
		Employee operator4 = Employee.buildOperator();
		Employee operator5 = Employee.buildOperator();
		Employee operator6 = Employee.buildOperator();
		Employee supervisor1 = Employee.buildSupervisor();
		Employee supervisor2 = Employee.buildSupervisor();
		Employee supervisor3 = Employee.buildSupervisor();
		Employee director = Employee.buildDirector();
		return Arrays.asList(operator1, operator2, operator3, operator4, operator5, operator6, supervisor1, supervisor2,
				supervisor3, director);
	}
}
