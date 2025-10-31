package clases;

public class CuentaAhorro extends CuentaBancaria {


    private double tipoInteresAnual;

    public CuentaAhorro(double tipoInteresAnual, Persona titular, double saldo, String IBAN) {
        super(titular, saldo, IBAN);
        if (tipoInteresAnual < 0) {
            throw new IllegalArgumentException("El tipo de interés no puede ser negativo.");
        }
        this.tipoInteresAnual = tipoInteresAnual;
    }


    public double getTipoInteresAnual() {
        return tipoInteresAnual;
    }

    public void setTipoInteresAnual(double tipoInteresAnual) {
        if (tipoInteresAnual < 0) {
            throw new IllegalArgumentException("El tipo de interés no puede ser negativo.");
        }
        this.tipoInteresAnual = tipoInteresAnual;
    }


    public double calcularInteresAnual() {
        return getSaldo() * tipoInteresAnual / 100;
    }


    @Override
    public String devolverInfoString() {
        return super.devolverInfoString() +
                ", tipoInteresAnual=" + tipoInteresAnual + "%" +
                ", interesAnualEstimado=" + String.format("%.2f", calcularInteresAnual()) + "€";
    }
}
