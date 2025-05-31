/*
 * Proyecto: División de Polinomios en Consola utilizando Listas Enlazadas
 * Representa polinomios mediante listas enlazadas y permite ingresar ejemplos predefinidos o personalizados.
 * Se visualiza el proceso de división paso a paso y se comprueba al finalizar que:
 *     dividendo = (divisor × cociente) + residuo
 */

import java.util.Scanner;

// Clase que representa un término del polinomio (nodo de la lista enlazada)
class NodoPolinomio {
    int coeficiente;
    int exponente;
    NodoPolinomio siguiente;

    public NodoPolinomio(int coeficiente, int exponente) {
        this.coeficiente = coeficiente;
        this.exponente = exponente;
        this.siguiente = null;
    }
}

// Clase para manejar polinomios como listas enlazadas
class PolinomioLista {
    NodoPolinomio cabeza;

    // Agrega un término ordenado por exponente (de mayor a menor); si ya existe, se suma el coeficiente.
    public void agregarTermino(int coeficiente, int exponente) {
        if (coeficiente == 0)
            return;
        NodoPolinomio nuevo = new NodoPolinomio(coeficiente, exponente);
        if (cabeza == null || cabeza.exponente < exponente) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        } else {
            NodoPolinomio actual = cabeza, anterior = null;
            while (actual != null && actual.exponente > exponente) {
                anterior = actual;
                actual = actual.siguiente;
            }
            if (actual != null && actual.exponente == exponente) {
                actual.coeficiente += coeficiente;
            } else {
                nuevo.siguiente = actual;
                if (anterior != null) {
                    anterior.siguiente = nuevo;
                } else {
                    cabeza = nuevo;
                }
            }
        }
    }

    // Imprime el polinomio en un formato legible
    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        NodoPolinomio actual = cabeza;
        while (actual != null) {
            if (sb.length() > 0)
                sb.append(actual.coeficiente > 0 ? " + " : " - ");
            else if (actual.coeficiente < 0)
                sb.append("-");
            int coef = Math.abs(actual.coeficiente);
            if (coef != 1 || actual.exponente == 0)
                sb.append(coef);
            if (actual.exponente > 0) {
                sb.append("x");
                if (actual.exponente > 1)
                    sb.append("^").append(actual.exponente);
            }
            actual = actual.siguiente;
        }
        return sb.length() > 0 ? sb.toString() : "0";
    }

    // División larga de polinomios con visualización paso a paso
    public static DivisionResult dividir(PolinomioLista dividendo, PolinomioLista divisor) {
        PolinomioLista cociente = new PolinomioLista();
        PolinomioLista residuo = copiar(dividendo);

        System.out.println("\n>>> Iniciando la división...");
        while (residuo.cabeza != null && residuo.cabeza.exponente >= divisor.cabeza.exponente) {
            int coef = residuo.cabeza.coeficiente / divisor.cabeza.coeficiente;
            int exp = residuo.cabeza.exponente - divisor.cabeza.exponente;
            System.out.println("\nResiduo actual: " + residuo.imprimir());
            System.out.println("Término calculado para el cociente: " + coef + "x^" + exp);
            
            cociente.agregarTermino(coef, exp);
            
            PolinomioLista producto = multiplicar(divisor, coef, exp);
            System.out.println("Divisor multiplicado: " + producto.imprimir());
            
            residuo = restar(residuo, producto);
            System.out.println("Nuevo residuo: " + residuo.imprimir());
        }
        System.out.println("\n>>> División finalizada.");
        return new DivisionResult(cociente, residuo);
    }

    // Multiplica un polinomio por un término (coeficiente * x^exponente)
    private static PolinomioLista multiplicar(PolinomioLista pol, int coef, int exp) {
        PolinomioLista resultado = new PolinomioLista();
        NodoPolinomio actual = pol.cabeza;
        while (actual != null) {
            resultado.agregarTermino(actual.coeficiente * coef, actual.exponente + exp);
            actual = actual.siguiente;
        }
        return resultado;
    }

    // Resta dos polinomios
    private static PolinomioLista restar(PolinomioLista p1, PolinomioLista p2) {
        PolinomioLista resultado = copiar(p1);
        NodoPolinomio actual = p2.cabeza;
        while (actual != null) {
            resultado.agregarTermino(-actual.coeficiente, actual.exponente);
            actual = actual.siguiente;
        }
        return resultado;
    }

    // Copia un polinomio
    private static PolinomioLista copiar(PolinomioLista origen) {
        PolinomioLista copia = new PolinomioLista();
        NodoPolinomio actual = origen.cabeza;
        while (actual != null) {
            copia.agregarTermino(actual.coeficiente, actual.exponente);
            actual = actual.siguiente;
        }
        return copia;
    }
    
    // Multiplicación de polinomios
    public static PolinomioLista multiplicarPolinomios(PolinomioLista p1, PolinomioLista p2) {
        PolinomioLista resultado = new PolinomioLista();
        NodoPolinomio actual1 = p1.cabeza;
        while (actual1 != null) {
            NodoPolinomio actual2 = p2.cabeza;
            while (actual2 != null) {
                resultado.agregarTermino(actual1.coeficiente * actual2.coeficiente,
                        actual1.exponente + actual2.exponente);
                actual2 = actual2.siguiente;
            }
            actual1 = actual1.siguiente;
        }
        return resultado;
    }
    
    // Suma de polinomios
    public static PolinomioLista sumarPolinomios(PolinomioLista p1, PolinomioLista p2) {
        PolinomioLista resultado = copiar(p1);
        NodoPolinomio actual = p2.cabeza;
        while (actual != null) {
            resultado.agregarTermino(actual.coeficiente, actual.exponente);
            actual = actual.siguiente;
        }
        return resultado;
    }
}

// Clase para almacenar el resultado de la división (cociente y residuo)
class DivisionResult {
    PolinomioLista cociente;
    PolinomioLista residuo;

    public DivisionResult(PolinomioLista cociente, PolinomioLista residuo) {
        this.cociente = cociente;
        this.residuo = residuo;
    }
}

// Clase principal que ejecuta el programa
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ASCII Banner
        System.out.println("=======================================");
        System.out.println("         DIVISIÓN DE POLINOMIOS        ");
        System.out.println("=======================================\n");

        // Menú de opciones
        System.out.println("Seleccione una opción:");
        System.out.println("1. Ejemplo 1");
        System.out.println("   Dividend: 2x^3 + 3x^2 - x + 5");
        System.out.println("   Divisor:  x^2 - 1");
        System.out.println("2. Ejemplo 2");
        System.out.println("   Dividend: 3x^4 - 2x^2 + x - 7");
        System.out.println("   Divisor:  x^2 - 2x + 1");
        System.out.println("3. Ingresar polinomios personalizados");
        System.out.print(">> ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        PolinomioLista dividendo = new PolinomioLista();
        PolinomioLista divisor = new PolinomioLista();

        switch (opcion) {
            case 1:
                // Ejemplo 1
                dividendo.agregarTermino(2, 3);
                dividendo.agregarTermino(3, 2);
                dividendo.agregarTermino(-1, 1);
                dividendo.agregarTermino(5, 0);
                divisor.agregarTermino(1, 2);
                divisor.agregarTermino(-1, 0);
                break;
            case 2:
                // Ejemplo 2
                dividendo.agregarTermino(3, 4);
                dividendo.agregarTermino(-2, 2);
                dividendo.agregarTermino(1, 1);
                dividendo.agregarTermino(-7, 0);
                divisor.agregarTermino(1, 2);
                divisor.agregarTermino(-2, 1);
                divisor.agregarTermino(1, 0);
                break;
            case 3:
                // Ingreso personalizado con guía detallada
                System.out.println("\n>>> Ingrese el polinomio dividendo");
                System.out.println("Escriba cada término en el formato: coeficiente exponente");
                System.out.println("Ejemplo: '2 3' representa 2x^3");
                System.out.println("Para finalizar, presione Enter sin escribir");
                ingresarPolinomio(scanner, dividendo);
                System.out.println("\n>>> Ingrese el polinomio divisor");
                System.out.println("Siga el mismo formato: coeficiente exponente");
                System.out.println("Ejemplo: '1 2' representa x^2");
                System.out.println("Para finalizar, presione Enter sin escribir");
                ingresarPolinomio(scanner, divisor);
                break;
            default:
                System.out.println("Opción no válida, se usará Ejemplo 1 por defecto.");
                dividendo.agregarTermino(2, 3);
                dividendo.agregarTermino(3, 2);
                dividendo.agregarTermino(-1, 1);
                dividendo.agregarTermino(5, 0);
                divisor.agregarTermino(1, 2);
                divisor.agregarTermino(-1, 0);
        }

        // Mostrar los polinomios seleccionados
        System.out.println("\nPolinomios seleccionados:");
        System.out.println("Dividendo: " + dividendo.imprimir());
        System.out.println("Divisor:   " + divisor.imprimir());

        // Realizar la división y mostrar el proceso paso a paso
        DivisionResult resultado = PolinomioLista.dividir(dividendo, divisor);

        // Resultado final
        System.out.println("\nResultado final:");
        System.out.println("Cociente: " + resultado.cociente.imprimir());
        System.out.println("Residuo:  " + resultado.residuo.imprimir());

        // Comprobación de la división: Verificar que dividendo = (divisor x cociente) + residuo
        comprobarDivision(dividendo, divisor, resultado.cociente, resultado.residuo);

        System.out.println("\nFin del proceso.");
    }

    // Método para ingresar polinomios desde la consola sin necesidad de escribir "0 0"
    public static void ingresarPolinomio(Scanner scanner, PolinomioLista polinomio) {
        while (true) {
            System.out.print("Ingrese un término (coeficiente exponente), o presione Enter para finalizar: ");
            String linea = scanner.nextLine();
            if (linea.trim().isEmpty()) {
                break;
            }
            String[] tokens = linea.trim().split("\\s+");
            if (tokens.length < 2) {
                System.out.println("Entrada inválida. Debe contener dos números separados por un espacio.");
                continue;
            }
            try {
                int coef = Integer.parseInt(tokens[0]);
                int exp = Integer.parseInt(tokens[1]);
                polinomio.agregarTermino(coef, exp);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: no se pudo convertir a número. Intente nuevamente.");
            }
        }
    }
    
    // Módulo para comprobar la división verificando que: dividendo = (divisor × cociente) + residuo
    public static void comprobarDivision(PolinomioLista original, PolinomioLista divisor,
                                          PolinomioLista cociente, PolinomioLista residuo) {
        PolinomioLista producto = PolinomioLista.multiplicarPolinomios(divisor, cociente);
        PolinomioLista suma = PolinomioLista.sumarPolinomios(producto, residuo);
        
        System.out.println("\nComprobando la división...");
        System.out.println("Producto (Divisor x Cociente): " + producto.imprimir());
        System.out.println("Suma del producto y residuo:   " + suma.imprimir());
        System.out.println("Polinomio original (Dividendo): " + original.imprimir());
        
        if (suma.imprimir().equals(original.imprimir()))
            System.out.println("La comprobación es correcta: dividendo = divisor x cociente + residuo.");
        else
            System.out.println("La comprobación es incorrecta.");
    }
}