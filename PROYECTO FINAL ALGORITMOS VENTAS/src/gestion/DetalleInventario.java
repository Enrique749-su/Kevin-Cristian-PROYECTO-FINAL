package gestion;
import java.io.Serializable;

public class DetalleInventario implements Serializable {
    private static final long serialVersionUID = 1L;
    public Producto producto;
    public int cantidad;
    public Bodega bodega;
    public DetalleInventario(Producto producto, int cantidad, Bodega bodega) {
        this.producto=producto; this.cantidad=cantidad; this.bodega=bodega;
    }
}
