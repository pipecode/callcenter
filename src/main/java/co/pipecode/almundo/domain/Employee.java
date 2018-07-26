package co.pipecode.almundo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

/**
 * Employee Domain
 */
public class Employee implements Runnable {

	private EmployeeType employeeType;
	private EmployeeStatus employeeStatus;

	private ConcurrentLinkedDeque<Call> incomingCalls;
	private ConcurrentLinkedDeque<Call> attendedCalls;

	public Employee() {
		this(EmployeeType.OPERATOR);
	}

	/**
	 * Crea un empleado disponible
	 * 
	 * @param employeeType
	 *            el tipo de empleado
	 */
	public Employee(EmployeeType employeeType) {
		Assert.notNull(employeeType, "El tipo de empleado no puede ser null");
		this.employeeType = employeeType;
		this.employeeStatus = EmployeeStatus.AVAILABLE;
		this.incomingCalls = new ConcurrentLinkedDeque<>();
		this.attendedCalls = new ConcurrentLinkedDeque<>();
	}

	/**
	 * Metodo que permite atender una llamada
	 * 
	 * @param call
	 *            la llamada a atender
	 */
	public synchronized void attend(Call call) {
		System.out.println("Empleado " + Thread.currentThread().getName() + " pone en cola llamada de "
				+ call.getDuration() + " segundos");
		this.incomingCalls.add(call);
	}

	/**
	 * Metodo que permite cambiar el estado de un empleado (Disponible o Ocupado)
	 * 
	 * @param employeeStatus
	 */
	private synchronized void setEmployeeState(EmployeeStatus employeeStatus) {
		System.out.println(
				"Empleado " + Thread.currentThread().getName() + " cambio su estado a " + employeeStatus.getEstado());
		this.employeeStatus = employeeStatus;
	}

	/**
	 * Metodo que permite obtener la lista de llamadas atendidas
	 * 
	 * @return la lista de llamadas
	 */
	public synchronized List<Call> getAttendedCalls() {
		return new ArrayList<>(attendedCalls);
	}

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public synchronized EmployeeStatus getEmployeeState() {
		return employeeStatus;
	}

	/**
	 * Metodo que permite construir un empleado de tipo Operador
	 * 
	 * @return el empleado operador
	 */
	public static Employee buildOperator() {
		return new Employee(EmployeeType.OPERATOR);
	}

	/**
	 * Metodo que permite construir un empleado de tipo Supervisor
	 * 
	 * @return el empleado supervisor
	 */
	public static Employee buildSupervisor() {
		return new Employee(EmployeeType.SUPERVISOR);
	}

	/**
	 * Metodo que permite construir un empleado de tipo Director
	 * 
	 * @return el empleado director
	 */
	public static Employee buildDirector() {
		return new Employee(EmployeeType.DIRECTOR);
	}

	/**
	 * Metodo que corre el hilo Si existen llamadas, cambia su estado de disponible
	 * a ocupado Cuando finaliza una llamada cambia su estado de ocupado a
	 * disponible Esto permite que los subprocesos decidan enviar la llamada a un
	 * empleado
	 */
	@Override
	public void run() {
		System.out.println("Employee " + Thread.currentThread().getName() + " inicia su trabajo");
		while (true) {
			if (!this.incomingCalls.isEmpty()) {
				Call call = this.incomingCalls.poll();
				this.setEmployeeState(EmployeeStatus.BUSY);
				System.out.println("Empleado " + Thread.currentThread().getName() + " atiende una llamada de "
						+ call.getDuration() + " segundos");
				try {
					TimeUnit.SECONDS.sleep(call.getDuration());
				} catch (InterruptedException e) {
					System.out.println("Employee " + Thread.currentThread().getName()
							+ " no pudo terminar la llamada de " + call.getDuration() + " segundos");
				} finally {
					this.setEmployeeState(EmployeeStatus.AVAILABLE);
				}
				this.attendedCalls.add(call);
				System.out.println("Employee " + Thread.currentThread().getName() + " finaliza llamada de "
						+ call.getDuration() + " segundos");
			}
		}
	}

}
