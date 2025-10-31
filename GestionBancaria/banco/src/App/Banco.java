package App;

import clases.CuentaBancaria;
import clases.CuentaCorrienteEmpresa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Banco {

    private final List<CuentaBancaria> cuentas;


    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public boolean abrirCuenta(CuentaBancaria c) {
        if (c == null) {
            System.out.println("No se puede añadir una cuenta nula.");
            return false;
        }

        if (buscarCuenta(c.getIBAN()) != null) {
            System.out.println("Ya existe una cuenta con ese IBAN.");
            return false;
        }

        cuentas.add(c);
        return true;
    }

    public String[] listadoCuentas() {
        return cuentas.stream()
                .map(CuentaBancaria::devolverInfoString)
                .toArray(String[]::new);
    }

    public String informacionCuenta(String IBAN) {
        CuentaBancaria c = buscarCuenta(IBAN);
        return (c != null) ? c.devolverInfoString() : null;
    }

    public boolean ingresoCuenta(String IBAN, double cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            return false;
        }

        CuentaBancaria c = buscarCuenta(IBAN);
        if (c != null) {
            c.setSaldo(c.getSaldo() + cantidad);
            return true;
        }

        System.out.println("No existe la cuenta especificada.");
        return false;
    }


    public boolean retiradaCuenta(String IBAN, double cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            return false;
        }

        CuentaBancaria c = buscarCuenta(IBAN);
        if (c == null) {
            System.out.println("No existe la cuenta especificada.");
            return false;
        }

        boolean sePuedeRetirar = false;

        // Caso general: saldo suficiente
        if (c.getSaldo() - cantidad >= 0) {
            sePuedeRetirar = true;
        }
        // Caso especial: CuentaCorrienteEmpresa con descubierto permitido
        else if (c instanceof CuentaCorrienteEmpresa empresa) {
            double descubiertoPermitido = empresa.getMaximoDescubierto();
            if (Math.abs(c.getSaldo() - cantidad) <= descubiertoPermitido) {
                sePuedeRetirar = true;
            }
        }

        if (sePuedeRetirar) {
            c.setSaldo(c.getSaldo() - cantidad);
            return true;
        } else {
            System.out.println("No hay suficiente saldo ni descubierto permitido.");
            return false;
        }
    }


    public double obtenerSaldo(String IBAN) {
        CuentaBancaria c = buscarCuenta(IBAN);
        return (c != null) ? c.getSaldo() : -1;
    }


    public boolean eliminarCuenta(String IBAN) {
        CuentaBancaria c = buscarCuenta(IBAN);
        if (c != null && c.getSaldo() == 0) {
            cuentas.remove(c);
            return true;
        }
        System.out.println("No se puede eliminar: saldo distinto de 0 o cuenta inexistente.");
        return false;
    }


    private CuentaBancaria buscarCuenta(String IBAN) {
        if (IBAN == null) return null;
        return cuentas.stream()
                .filter(c -> IBAN.equals(c.getIBAN()))
                .findFirst()
                .orElse(null);
    }


    public void guardarCuentas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datoscuentasbancarias.dat"))) {
            for (CuentaBancaria cuenta : cuentas) {
                oos.writeObject(cuenta);
            }
            System.out.println("Cuentas guardadas correctamente.");
        } catch (IOException ex) {
            System.out.println("Error al guardar las cuentas: " + ex.getMessage());
        }
    }


    public void recuperarCuentas() {
        File archivo = new File("datoscuentasbancarias.dat");
        if (!archivo.exists()) {
            System.out.println("No se encontró el fichero de cuentas. Se iniciará con una lista vacía.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            while (true) {
                try {
                    CuentaBancaria cuenta = (CuentaBancaria) ois.readObject();
                    this.abrirCuenta(cuenta);
                } catch (EOFException eof) {
                    break; // fin del fichero
                }
            }
            System.out.println("Cuentas cargadas desde fichero correctamente.");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error al recuperar cuentas: " + ex.getMessage());
        }
    }

    public void generarListado() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ListadoClientesCCC.txt"))) {
            for (CuentaBancaria cuenta : cuentas) {
                bw.write(cuenta.getTitular().devolverInfoString() + " " + cuenta.getIBAN());
                bw.newLine();
            }
            bw.write("Número total de cuentas: " + cuentas.size());
            System.out.println("Listado de clientes generado correctamente.");
        } catch (IOException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
