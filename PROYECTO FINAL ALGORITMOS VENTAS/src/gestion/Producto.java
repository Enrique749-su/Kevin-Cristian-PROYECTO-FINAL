package gestion;
import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    public String codigo, nombre, descripcion, marca;
    public double precio;
    public Producto(String codigo, String nombre, String descripcion, String marca, double precio) {
        this.codigo=codigo; this.nombre=nombre; this.descripcion=descripcion; this.marca=marca; this.precio=precio;
    }
    public String toString() { return codigo + " - " + nombre; }
}
