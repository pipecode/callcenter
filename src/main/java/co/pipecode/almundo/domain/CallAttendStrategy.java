package co.pipecode.almundo.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

/**
 * CallAttendStrategy
 * 
 */
public class CallAttendStrategy {

	/**
	 * Metodo que retorna el primer operador disponible Si todos los operadores
	 * estan ocupados, retorna el primer supervisor disponible Si todos los
	 * supervisores estan ocupados, retorna el primer director disponible Si todos
	 * los empleados estan ocupados, retorna null
	 * 
	 * @param availableEmployees
	 *            la lista de empleados disponibles
	 * @return el primer empleado disponible
	 */
	public Employee findAvailableEmployee(Collection<Employee> availableEmployees) {
		Assert.notNull(availableEmployees, "La lista de empleados disponibles no puede estar vacia");
		List<Employee> employess = availableEmployees.stream()
				.filter(e -> e.getEmployeeState().equals(EmployeeStatus.AVAILABLE)).collect(Collectors.toList());
		Optional<Employee> employee = employess.stream().filter(e -> e.getEmployeeType().equals(EmployeeType.OPERATOR))
				.findAny();
		if (!employee.isPresent()) {
			System.out.println("No hay operadores disponibles");
			employee = employess.stream().filter(e -> e.getEmployeeType().equals(EmployeeType.SUPERVISOR)).findAny();
			if (!employee.isPresent()) {
				System.out.println("No hay supervisores disponibles");
				employee = employess.stream().filter(e -> e.getEmployeeType().equals(EmployeeType.DIRECTOR)).findAny();
				if (!employee.isPresent()) {
					System.out.println("No hay directores disponibles");
					return null;
				}
			}
		}
		System.out.println("Empleado " + employee.get().getEmployeeType() + " disponible");
		return employee.get();
	}
}
