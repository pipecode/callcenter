package co.pipecode.almundo.domain;

/**
 * EmployeeStatus Enum
 * 
 * Enumerador con los estados de un empleado
 */
public enum EmployeeStatus {

	AVAILABLE("Disponible"), BUSY("Ocupado");

	private final String estado;

	EmployeeStatus(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

}
