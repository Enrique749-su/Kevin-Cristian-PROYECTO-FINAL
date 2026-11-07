package gestion;
import java.io.Serializable;

public class DetalleOrden implements Serializable {
    private static final long serialVersionUID = 1L;
    public Producto producto;
    public int cantidad;
    public double subtotal;
    public DetalleOrden(Producto producto, int cantidad) {
        this.producto=producto; this.cantidad=cantidad; this.subtotal=producto.precio*cantidad;
    }
}
