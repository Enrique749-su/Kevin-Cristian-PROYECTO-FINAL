package gestion;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class OrdenCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    public String codigo, estado;
    public Cliente cliente;
    public List<DetalleOrden> productos = new ArrayList<>();
    public LocalDate fecha;
    public double total;
    public OrdenCompra(String codigo, Cliente cliente) {
        this.codigo=codigo; this.cliente=cliente; this.fecha=LocalDate.now(); this.estado="Pendiente";
    }
    public void agregarProducto(Producto p, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        productos.add(new DetalleOrden(p,cantidad)); recalcular();
    }
    public void recalcular() { total=productos.stream().mapToDouble(d -> d.subtotal).sum(); }
    public String resumen() { return codigo + " | " + cliente.nombre + " | " + fecha + " | Q" + String.format("%.2f",total) + " | " + estado; }
}
