package gestion;
import java.io.Serializable;

public class Bodega implements Serializable {
    private static final long serialVersionUID = 1L;
    public String idBodega, direccion;
    public Empleado empleado;
    public double capacidad;
    public Bodega(String idBodega, Empleado empleado, double capacidad, String direccion) {
        this.idBodega=idBodega; this.empleado=empleado; this.capacidad=capacidad; this.direccion=direccion;
    }
    public String toString() { return idBodega + " - " + direccion; }
}
