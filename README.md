# Ejercicio Call Center

### Consigna

Existe un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada telefónica en primera instancia debe ser atendida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no haber tampoco supervisores libres debe ser atendida por un director.

### Requerimientos

* Debe existir una clase Dispatcher encargada de manejar las llamadas, y debe contener el método dispatchCall para que las asigne a los empleados disponibles.
* El método dispatchCall puede invocarse por varios hilos al mismo tiempo.
* La clase Dispatcher debe tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente).</p>
* Cada llamada puede durar un tiempo aleatorio entre 5 y 10 segundos.
* Debe tener un test unitario donde lleguen 10 llamadas.

### Extras/Plus

* Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
* Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
* Agregar los tests unitarios que se crean convenientes.
* Agregar documentación de código

### Solucion Extras/Plus

Se agregan listas concurrentes con la clase `ConcurrentLinkedDeque` para que los distintos subprocesos añadan o eliminen elementos de las listas a la vez sin producir ninguna inconsistencia de datos. Esto también nos ayuda a que las operaciones que no se pueden hacer de inmediato, emitan una excepción o retornen un valor nulo según la operación y evite bloqueos.
Para manejar más llamadas de las que los empleados pueden manejar, se colocan en una cola concurrente y esperan hasta que algún empleado esté disponible.

El uso de estas listas resuelven los puntos uno y dos de los (Extras/Plus) propuestos, permitiendo que se liberen las llamadas cuando se desocupe un empleado.

### Requisitos Técnicos
Java JDK 8 y Maven son necesarios

### Compilación
`mvn clean install -U`

### Ejecución de pruebas
`mvn test -B`

### Autor
[Luis Felipe Castro Ospina](https://www.linkedin.com/in/felipecode/)