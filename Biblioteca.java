import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Biblioteca {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        GestorPrestamos gestor = new GestorPrestamos(new PrestamoMemoriaDAO());

        while (true) {
            System.out.println("\nBiblioteca");
            System.out.println("1. Prestamo normal (7 dias)");
            System.out.println("2. Prestamo corto (2 dias)");
            System.out.println("3. Ver prestamos");
            System.out.println("4. Calcular multa");
            System.out.println("5. Salir");
            System.out.print("Opcion: ");
            String opcion = teclado.nextLine();

            if (opcion.equals("5")) {
                System.out.println("Fin del programa.");
                break;
            } else if (opcion.equals("3")) {
                gestor.mostrarPrestamos();
            } else if (opcion.equals("4")) {
                gestor.mostrarPrestamos();
                if (gestor.cantidad() > 0) {
                    try {
                        System.out.print("Numero de prestamo: ");
                        int numero = Integer.parseInt(teclado.nextLine());
                        System.out.print("Dias de retraso: ");
                        int dias = Integer.parseInt(teclado.nextLine());
                        gestor.mostrarMulta(numero, dias);
                    } catch (NumberFormatException e) {
                        System.out.println("Debes ingresar numeros enteros.");
                    }
                }
            } else if (opcion.equals("1") || opcion.equals("2")) {
                System.out.print("Libro: ");
                String libro = teclado.nextLine();
                System.out.print("Lector: ");
                String lector = teclado.nextLine();

                if (libro.trim().isEmpty() || lector.trim().isEmpty()) {
                    System.out.println("Debes ingresar libro y lector.");
                } else {
                    CreadorPrestamo creador;
                    if (opcion.equals("1")) {
                        creador = new CreadorNormal();
                    } else {
                        creador = new CreadorCorto();
                    }
                    creador.registrar(gestor, libro, lector);
                }
            } else {
                System.out.println("Opcion no valida.");
            }
        }
        teclado.close();
    }
}

abstract class Prestamo {
    private final String libro;
    private final String lector;
    private final EstrategiaMulta multa;

    public Prestamo(String libro, String lector, EstrategiaMulta multa) {
        this.libro = libro;
        this.lector = lector;
        this.multa = multa;
    }

    public String getLibro() { return libro; }
    public String getLector() { return lector; }
    public abstract int diasPermitidos();

    public int calcularMulta(int diasRetraso) {
        return multa.calcular(diasRetraso);
    }
}

class PrestamoNormal extends Prestamo {
    public PrestamoNormal(String libro, String lector) {
        super(libro, lector, new MultaNormal());
    }

    public int diasPermitidos() { return 7; }
}

class PrestamoCorto extends Prestamo {
    public PrestamoCorto(String libro, String lector) {
        super(libro, lector, new MultaCorta());
    }

    public int diasPermitidos() { return 2; }
}

/* Creacional - Factory Method: cada creador decide que tipo de prestamo construir.
   Asi el menu solicita el prestamo sin llamar directamente a sus constructores. */
abstract class CreadorPrestamo {
    public abstract Prestamo crear(String libro, String lector);

    public void registrar(GestorPrestamos gestor, String libro, String lector) {
        gestor.registrar(crear(libro, lector));
    }
}

class CreadorNormal extends CreadorPrestamo {
    public Prestamo crear(String libro, String lector) {
        return new PrestamoNormal(libro, lector);
    }
}

class CreadorCorto extends CreadorPrestamo {
    public Prestamo crear(String libro, String lector) {
        return new PrestamoCorto(libro, lector);
    }
}

/* Estructural - DAO: el gestor pide guardar y consultar prestamos mediante esta interfaz.
   PrestamoMemoriaDAO maneja la lista sin mezclarla con las reglas de la biblioteca. */
interface PrestamoDAO {
    void guardar(Prestamo prestamo);
    List<Prestamo> obtener();
}

class PrestamoMemoriaDAO implements PrestamoDAO {
    private final List<Prestamo> prestamos = new ArrayList<>();

    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public List<Prestamo> obtener() {
        return new ArrayList<>(prestamos);
    }
}

/* Comportamiento - Strategy: permite calcular multas con diferentes reglas.
   MultaNormal tiene un dia de tolerancia y MultaCorta cobra desde el primer dia.
   Prestamo utiliza la interfaz EstrategiaMulta sin cambiar su codigo. */
interface EstrategiaMulta {
    int calcular(int diasRetraso);
}

class MultaNormal implements EstrategiaMulta {
    public int calcular(int diasRetraso) {
        if (diasRetraso <= 1) {
            return 0;
        }
        return (diasRetraso - 1) * 2;
    }
}

class MultaCorta implements EstrategiaMulta {
    public int calcular(int diasRetraso) {
        return diasRetraso * 3;
    }
}

class GestorPrestamos {
    private final PrestamoDAO registro;

    public GestorPrestamos(PrestamoDAO registro) {
        this.registro = registro;
    }

    public void registrar(Prestamo prestamo) {
        registro.guardar(prestamo);
        System.out.println("Registrado: " + prestamo.getLibro() + " | Lector: " + prestamo.getLector() + " | Dias: " + prestamo.diasPermitidos());
    }

    public int cantidad() {
        return registro.obtener().size();
    }

    public void mostrarPrestamos() {
        List<Prestamo> prestamos = registro.obtener();
        if (prestamos.isEmpty()) {
            System.out.println("No hay prestamos.");
        } else {
            for (int i = 0; i < prestamos.size(); i++) {
                Prestamo prestamo = prestamos.get(i);
                System.out.println((i + 1) + ". " + prestamo.getLibro() + " | " + prestamo.getLector() + " | " + prestamo.diasPermitidos() + " dias");
            }
        }
    }

    public void mostrarMulta(int numero, int diasRetraso) {
        List<Prestamo> prestamos = registro.obtener();
        if (numero < 1 || numero > prestamos.size() || diasRetraso < 0) {
            System.out.println("Numero o dias no validos.");
        } else {
            Prestamo prestamo = prestamos.get(numero - 1);
            System.out.println("Multa de " + prestamo.getLibro() + ": Bs " + prestamo.calcularMulta(diasRetraso));
        }
    }
}
