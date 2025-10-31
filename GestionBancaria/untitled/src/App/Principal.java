package App;

import clases.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Principal {

    private static final Scanner sn = new Scanner(System.in).useDelimiter("\n").useLocale(Locale.US);
    private static final Banco banco = new Banco();

    public static void main(String[] args) {
        banco.recuperarCuentas();
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opción del menú (1-9):");

            try {
                switch (opcion) {
                    case 1 -> abrirCuenta();
                    case 2 -> listarCuentas();
                    case 3 -> mostrarInformacionCuenta();
                    case 4 -> ingresarEnCuenta();
                    case 5 -> retirarDeCuenta();
                    case 6 -> consultarSaldo();
                    case 7 -> eliminarCuenta();
                    case 8 -> generarListadoClientes();
                    case 9 -> {
                        salir = true;
                        banco.guardarCuentas();
                        System.out.println("Cuentas guardadas correctamente. ¡Hasta pronto!");
                    }
                    default -> System.out.println("Opción no válida. Intenta nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Abrir una nueva cuenta");
        System.out.println("2. Ver listado de cuentas disponibles");
        System.out.println("3. Obtener datos de una cuenta concreta");
        System.out.println("4. Realizar un ingreso");
        System.out.println("5. Retirar efectivo");
        System.out.println("6. Consultar saldo actual");
        System.out.println("7. Eliminar una cuenta");
        System.out.println("8. Generar listado de clientes");
        System.out.println("9. Salir");
        System.out.println("=======================");
    }

    private static void abrirCuenta() throws Exception {
        System.out.println("\n=== Apertura de nueva cuenta ===");
        String nombre = leerTexto("Nombre del titular:");
        String apellidos = leerTexto("Apellidos del titular:");
        String dni = leerDNI();
        Persona titular = new Persona(nombre, apellidos, dni);

        String IBAN = leerIBAN();
        double saldo = leerDouble("Saldo inicial:");

        System.out.println("Selecciona el tipo de cuenta:");
        System.out.println("1. Cuenta Ahorro");
        System.out.println("2. Cuenta Corriente Personal");
        System.out.println("3. Cuenta Corriente Empresa");
        int tipoCuenta = leerEntero("Opción:");

        CuentaBancaria cuenta;
        switch (tipoCuenta) {
            case 1 -> {
                double tipoInteres = leerDouble("Tipo de interés:");
                cuenta = new CuentaAhorro(tipoInteres, titular, saldo, IBAN);
            }
            case 2 -> {
                String entidades = leerTexto("Lista de entidades autorizadas:");
                double comision = leerDouble("Comisión de mantenimiento:");
                cuenta = new CuentaCorrientePersonal(comision, entidades, titular, saldo, IBAN);
            }
            case 3 -> {
                String entidades = leerTexto("Lista de entidades autorizadas:");
                double tipoDescubierto = leerDouble("Tipo de interés por descubierto:");
                double maxDescubierto = leerDouble("Máximo descubierto permitido:");
                double comisionDescubierto = leerDouble("Comisión por descubierto:");
                cuenta = new CuentaCorrienteEmpresa(maxDescubierto, tipoDescubierto, comisionDescubierto, entidades, titular, saldo, IBAN);
            }
            default -> throw new Exception("Tipo de cuenta no válido");
        }

        if (banco.abrirCuenta(cuenta)) {
            System.out.println("Cuenta abierta con éxito");
        } else {
            System.out.println("Error al abrir la cuenta (IBAN duplicado o datos inválidos)");
        }
    }

    private static void listarCuentas() {
        System.out.println("\n=== Listado de cuentas ===");
        String[] cuentas = banco.listadoCuentas();
        if (cuentas.length == 0) {
            System.out.println("No hay cuentas registradas.");
        } else {
            for (String c : cuentas) System.out.println(c);
        }
    }

    private static void mostrarInformacionCuenta() {
        String IBAN = leerIBAN();
        String info = banco.informacionCuenta(IBAN);
        System.out.println(info != null ? info : "La cuenta no existe.");
    }

    private static void ingresarEnCuenta() {
        String IBAN = leerIBAN();
        double cantidad = leerDouble("Cantidad a ingresar:");
        if (banco.ingresoCuenta(IBAN, cantidad)) {
            System.out.println("Ingreso realizado correctamente.");
        } else {
            System.out.println("No se pudo realizar el ingreso.");
        }
    }

    private static void retirarDeCuenta() {
        String IBAN = leerIBAN();
        double cantidad = leerDouble("Cantidad a retirar:");
        if (banco.retiradaCuenta(IBAN, cantidad)) {
            System.out.println("Retirada realizada correctamente.");
        } else {
            System.out.println("No se pudo realizar la retirada (saldo insuficiente o cuenta inexistente).");
        }
    }

    private static void consultarSaldo() {
        String IBAN = leerIBAN();
        double saldo = banco.obtenerSaldo(IBAN);
        System.out.println(saldo != -1 ? "Saldo actual: " + saldo + " €" : "La cuenta no existe.");
    }

    private static void eliminarCuenta() {
        String IBAN = leerIBAN();
        if (banco.eliminarCuenta(IBAN)) {
            System.out.println("Cuenta eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la cuenta (no existe).");
        }
    }

    private static void generarListadoClientes() {
        banco.generarListado();
        System.out.println("Listado de clientes generado correctamente.");
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje + " ");
        return sn.next().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " ");
                return sn.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduce un número entero válido.");
                sn.next();
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " ");
                return sn.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduce un número válido (usa punto decimal si es necesario).");
                sn.next();
            }
        }
    }

    private static String leerIBAN() {
        while (true) {
            String iban = leerTexto("Introduce el IBAN (formato ES00...):");
            if (iban.matches("^ES\\d{20}$")) {
                return iban;
            }
            System.out.println("Formato IBAN incorrecto. Debe comenzar por ES y tener 22 caracteres (incluido ES).");
        }
    }

    private static String leerDNI() {
        while (true) {
            String dni = leerTexto("Introduce el DNI:");
            if (dni.matches("\\d{8}[A-Za-z]")) {
                return dni;
            }
            System.out.println("Formato DNI incorrecto. Ejemplo: 12345678A");
        }
    }
}
