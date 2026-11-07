package gestion;
import java.io.Serializable;
import java.time.LocalDate;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    public String dpi, nombre, direccion, tipo;
    public LocalDate fechaNacimiento;
    public Cliente(String dpi, String nombre, String direccion, LocalDate fechaNacimiento, String tipo) {
        this.dpi=dpi; this.nombre=nombre; this.direccion=direccion; this.fechaNacimiento=fechaNacimiento; this.tipo=tipo;
    }
    public String toString() { return nombre + " (" + dpi + ")"; }
}
