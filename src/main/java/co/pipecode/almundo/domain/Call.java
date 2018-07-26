package co.pipecode.almundo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.util.Assert;

/**
 * Call Domain
 */
public class Call {

	private int duration;
	private int minDuracion = 5;
	private int maxDuracion = 10;

	/**
	 * Crea una nueva llamada con duracion en segundos
	 *
	 * @param duration
	 *            duration en segundos, debe ser mayor a 0
	 */
	public Call(int duration) {
		Assert.isTrue(duration > 0, "la duracion de la llamada debe ser mayor a 0");
		this.duration = duration;
	}

	/**
	 * Crea una nueva llamada con duracion en segundos y permite configurar valores
	 * de minimo y maximo para generar llamadas random
	 *
	 * @param defaultDuration
	 *            duration en segundos, debe ser mayor a 0
	 * @param minDuracion
	 *            duracion minima de una llamada
	 * @param maxDuracion
	 *            duracion maxima de una llamada
	 * 
	 */
	public Call(int defaultDuration, int minDuracion, int maxDuracion) {
		Assert.isTrue(defaultDuration > 0, "la duracion de la llamada debe ser mayor a 0");
		Assert.isTrue(minDuracion > 0 && maxDuracion > 0,
				"La duracion minima y maxima de la llamada debe ser mayor a 0");
		Assert.isTrue(maxDuracion >= minDuracion,
				"La duracion maxima de la llamada debe ser mayor a la duracion minima de la llamada");
		this.duration = defaultDuration;
		this.minDuracion = minDuracion;
		this.maxDuracion = maxDuracion;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Metodo que crea una llamada random
	 * 
	 * @return la llamada con una duracion al azar en segundos.
	 */
	public Call buildRandomCall() {
		return new Call(ThreadLocalRandom.current().nextInt(this.minDuracion, this.maxDuracion + 1));
	}

	/**
	 * Metodo que crea una lista de llamadas random
	 * 
	 * @param totalCalls
	 *            la cantidad de llamadas a generar
	 * @return la lista de llamadas generadas con una duracion en segundos
	 */
	public List<Call> buildRandomCalls(int totalCalls) {
		Assert.isTrue(totalCalls > 0, "La cantidad de llamadas a generar debe ser mayor a 0");
		List<Call> callList = new ArrayList<>();
		for (int i = 0; i < totalCalls; i++) {
			callList.add(buildRandomCall());
		}
		return callList;
	}
}
