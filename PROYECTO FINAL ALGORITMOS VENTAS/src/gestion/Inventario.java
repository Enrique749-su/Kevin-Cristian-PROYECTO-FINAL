package gestion;
import java.io.Serializable;
import java.util.*;

public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<DetalleInventario> existencias = new ArrayList<>();
    public int consultarStock(Producto p) {
        return existencias.stream().filter(x -> x.producto.codigo.equals(p.codigo)).mapToInt(x -> x.cantidad).sum();
    }
    public void agregarStock(Producto p, int cantidad, Bodega b) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        for (DetalleInventario d: existencias) {
            if (d.producto.codigo.equals(p.codigo) && d.bodega.idBodega.equals(b.idBodega)) { d.cantidad += cantidad; return; }
        }
        existencias.add(new DetalleInventario(p,cantidad,b));
    }
    public void descontarStock(Producto p, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad inválida.");
        if (consultarStock(p) < cantidad) throw new IllegalArgumentException("Stock insuficiente para " + p.nombre);
        int pendiente = cantidad;
        for (DetalleInventario d: existencias) {
            if (d.producto.codigo.equals(p.codigo) && pendiente > 0) {
                int quitar = Math.min(d.cantidad, pendiente);
                d.cantidad -= quitar; pendiente -= quitar;
            }
        }
    }
}
