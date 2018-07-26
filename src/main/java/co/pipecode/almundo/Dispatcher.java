package co.pipecode.almundo;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.util.Assert;

import co.pipecode.almundo.domain.Call;
import co.pipecode.almundo.domain.CallAttendStrategy;
import co.pipecode.almundo.domain.Employee;

/**
 * Dispatcher
 */
public class Dispatcher implements Runnable {

	private boolean active;

	private CallAttendStrategy callAttendStrategy;
	private ExecutorService executorService;

	private ConcurrentLinkedDeque<Employee> availableEmployees;
	private ConcurrentLinkedDeque<Call> incomingCalls;

	public Dispatcher(List<Employee> employees) {
		this(employees, new CallAttendStrategy(), 10);
	}

	public Dispatcher(List<Employee> employees, Integer maxThreads) {
		this(employees, new CallAttendStrategy(), maxThreads);
	}

	/**
	 * Crea y configura el despachador
	 * 
	 * @param employees
	 *            los empleados disponibles
	 * @param callAttendStrategy
	 *            la estrategia de procesamiento para empleados
	 * @param maxThreads
	 *            cantidad maxima del pool de hilos
	 */
	public Dispatcher(List<Employee> employees, CallAttendStrategy callAttendStrategy, Integer maxThreads) {
		Assert.notNull(employees, "La lista de empleados no puede ser null");
		Assert.notNull(callAttendStrategy, "Debe existir una estrategia para manejar los empleados");
		Assert.notNull(maxThreads, "La cantidad maxima de hilos no puede ser null");
		this.availableEmployees = new ConcurrentLinkedDeque<Employee>(employees);
		this.callAttendStrategy = callAttendStrategy;
		this.incomingCalls = new ConcurrentLinkedDeque<>();
		this.executorService = Executors.newFixedThreadPool(maxThreads);
	}

	/**
	 * Metodo que despacha las llamadas entrantes y las pone en la lista de
	 * incommingCalls
	 * 
	 * @param call
	 *            la llamada
	 * @throws InterruptedException
	 */
	public synchronized void dispatchCall(Call call) throws InterruptedException {
		System.out.println("Nueva llamada despachada de " + call.getDuration() + " segundos");
		this.incomingCalls.add(call);
	}

	/**
	 * Inicia los hilos de los empleados y permite correr el método de ejecución del
	 * despachador
	 */
	public synchronized void start() {
		setActive(true);
		for (Employee employee : this.availableEmployees) {
			this.executorService.execute(employee);
		}
	}

	/**
	 * Metodo que detiene inmediatamente los hilos de los empleados y el método de
	 * ejecución del despachador
	 */
	public synchronized void stop() {
		setActive(false);
		this.executorService.shutdown();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Metodo que corre el hilo Si existen llamadas entrantes, asigna a un operador
	 * disponible Las llamadas se pondran en cola hasta que existan operadores
	 * disponibles
	 */
	@Override
	public void run() {
		while (isActive()) {
			if (this.incomingCalls.isEmpty()) {
				continue;
			} else {
				Employee employee = this.callAttendStrategy.findAvailableEmployee(this.availableEmployees);
				if (employee == null) {
					continue;
				}
				Call call = this.incomingCalls.poll();
				try {
					employee.attend(call);
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					this.incomingCalls.addFirst(call);
				}
			}
		}
	}

}
