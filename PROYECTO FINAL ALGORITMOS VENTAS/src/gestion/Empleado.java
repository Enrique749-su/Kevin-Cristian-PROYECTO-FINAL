package gestion;
import java.io.Serializable;

public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    public String idEmpleado, nombre, dpi, direccion, puesto;
    public double salario;
    public Empleado(String idEmpleado, String nombre, String dpi, String direccion, String puesto, double salario) {
        this.idEmpleado=idEmpleado; this.nombre=nombre; this.dpi=dpi; this.direccion=direccion; this.puesto=puesto; this.salario=salario;
    }
    public String toString() { return idEmpleado + " - " + nombre; }
}
