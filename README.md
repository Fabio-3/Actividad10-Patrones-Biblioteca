# Biblioteca - Patrones de Diseño

Sistema de préstamos de biblioteca desarrollado en Java. Permite registrar préstamos, consultarlos y calcular multas por retraso.

## Tecnologías y requisitos

- Java JDK 11 o superior.
- Terminal o Visual Studio Code.
- No requiere librerías externas, interfaz gráfica ni base de datos.

## Descargar el proyecto

**Opción 1: descargar ZIP**

1. En este repositorio de GitHub, seleccionar **Code > Download ZIP**.
2. Extraer el archivo ZIP y abrir la carpeta que contiene `Biblioteca.java`.

**Opción 2: clonar con Git**

Si Git está instalado, abrir una terminal y ejecutar:

```bash
git clone https://github.com/Fabio-3/Actividad10-Patrones-Biblioteca.git
cd Actividad10-Patrones-Biblioteca
```

## Ejecutar el programa

Abrir una terminal dentro de la carpeta que contiene `Biblioteca.java` y escribir:

```bash
java Biblioteca.java
```

Con Java JDK 11 o superior, ese comando permite ejecutar el archivo directamente.

También se puede compilar y ejecutar en dos pasos:

```bash
javac Biblioteca.java
java Biblioteca
```

## Opciones del menú

1. **Préstamo normal:** registra un libro y su lector, con plazo de 7 días.
2. **Préstamo corto:** registra un libro y su lector, con plazo de 2 días.
3. **Ver préstamos:** muestra los préstamos guardados y sus números.
4. **Calcular multa:** pide el número del préstamo y cuántos días se superó el plazo permitido.
5. **Salir:** termina el programa.

Los préstamos se almacenan solo en memoria: al cerrar el programa, los registros se pierden.

## Reglas de multa

- **Préstamo normal:** tiene un día de tolerancia después del plazo. Desde el segundo día de retraso se cobran **Bs 2 por cada día adicional**.
- **Préstamo corto:** no tiene día de tolerancia. Se cobran **Bs 3 por día** desde el primer día de retraso.
- Con **0 días de retraso**, la multa es **Bs 0** para ambos tipos.

Los días de retraso se ingresan manualmente: el programa no calcula fechas de devolución ni realiza cobros.

## Ejemplo para probarlo

1. Seleccionar **1** y registrar el libro `Caballo de Troya` para `Fernando`.
2. Seleccionar **2** y registrar `Mil y una Noches` para `Marcelo`.
3. Seleccionar **3** para ver la lista de préstamos.
4. Seleccionar **4**, introducir el número `1` y `3` días de retraso: la multa será **Bs 4** (se descuenta un día de tolerancia).
5. Seleccionar nuevamente **4**, introducir el número `2` y `3` días de retraso: la multa será **Bs 9**.
6. Seleccionar **5** para salir.

## Patrones de diseño utilizados

| Categoría | Patrón | Aplicación |
| --- | --- | --- |
| Creacional | **Factory Method** | `CreadorPrestamo`, `CreadorNormal` y `CreadorCorto` organizan la creación de préstamos. |
| Estructural | **DAO** | `PrestamoDAO` y `PrestamoMemoriaDAO` separan el acceso a los datos del gestor. |
| Comportamiento | **Strategy** | `EstrategiaMulta`, `MultaNormal` y `MultaCorta` definen distintas reglas de multa. |

## Archivos

- `Biblioteca.java`: contiene todas las clases y el método `main` del programa.
- `README.md`: describe las funciones, los patrones y cómo descargar y ejecutar el proyecto.
