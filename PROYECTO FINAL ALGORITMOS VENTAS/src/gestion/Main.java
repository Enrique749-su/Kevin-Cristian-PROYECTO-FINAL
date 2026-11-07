package gestion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    private List<Producto> productos = Persistencia.cargar("productos.dat");
    private List<Cliente> clientes = Persistencia.cargar("clientes.dat");
    private List<Empleado> empleados = Persistencia.cargar("empleados.dat");
    private List<Bodega> bodegas = Persistencia.cargar("bodegas.dat");
    private List<OrdenCompra> ordenes = Persistencia.cargar("ordenes.dat");
    private Inventario inventario = cargarInventario();

    private DefaultTableModel tmProductos, tmClientes, tmEmpleados, tmBodegas, tmInventario, tmOrdenes;
    private JTable tbProductos, tbClientes, tbEmpleados, tbBodegas, tbInventario, tbOrdenes;

    private static Inventario cargarInventario() {
        List<Inventario> l=Persistencia.cargar("inventario.dat");
        return l.isEmpty()?new Inventario():l.get(0);
    }

    public Main() {
        super("Sistema de Gestión de Ventas e Inventario");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1050,650); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JLabel encabezado=new JLabel("  SISTEMA DE GESTIÓN DE VENTAS E INVENTARIO", JLabel.LEFT);
        encabezado.setOpaque(true); encabezado.setBackground(new Color(24,48,80)); encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("SansSerif",Font.BOLD,20)); encabezado.setBorder(BorderFactory.createEmptyBorder(18,12,18,12));
        add(encabezado,BorderLayout.NORTH);

        JTabbedPane tabs=new JTabbedPane();
        tabs.addTab("Productos", panelProductos());
        tabs.addTab("Clientes", panelClientes());
        tabs.addTab("Empleados", panelEmpleados());
        tabs.addTab("Bodegas", panelBodegas());
        tabs.addTab("Inventario", panelInventario());
        tabs.addTab("Órdenes", panelOrdenes());
        add(tabs,BorderLayout.CENTER);
        actualizarTodo();
    }

    private JPanel basePanel(JTable tabla, Runnable accion, String texto) {
        JPanel p=new JPanel(new BorderLayout(8,8)); p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton b=new JButton(texto); b.addActionListener(e->accion.run());
        JPanel sur=new JPanel(new FlowLayout(FlowLayout.LEFT)); sur.add(b); p.add(sur,BorderLayout.SOUTH);
        return p;
    }
    private DefaultTableModel modelo(String... cols) { return new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} }; }

    private JPanel panelProductos() {
        tmProductos=modelo("Código","Nombre","Descripción","Marca","Precio");
        tbProductos=new JTable(tmProductos);
        return basePanel(tbProductos,this::registrarProducto,"Registrar producto");
    }
    private JPanel panelClientes() {
        tmClientes=modelo("DPI","Nombre","Dirección","Nacimiento","Tipo");
        tbClientes=new JTable(tmClientes);
        return basePanel(tbClientes,this::registrarCliente,"Registrar cliente");
    }
    private JPanel panelEmpleados() {
        tmEmpleados=modelo("ID","Nombre","DPI","Dirección","Puesto","Salario");
        tbEmpleados=new JTable(tmEmpleados);
        return basePanel(tbEmpleados,this::registrarEmpleado,"Registrar empleado");
    }
    private JPanel panelBodegas() {
        tmBodegas=modelo("ID bodega","Responsable","Capacidad","Dirección");
        tbBodegas=new JTable(tmBodegas);
        return basePanel(tbBodegas,this::registrarBodega,"Registrar bodega");
    }
    private JPanel panelInventario() {
        tmInventario=modelo("Producto","Stock total","Bodega");
        tbInventario=new JTable(tmInventario);
        JPanel p=basePanel(tbInventario,this::registrarStock,"Agregar existencias");
        return p;
    }
    private JPanel panelOrdenes() {
        tmOrdenes=modelo("Código","Cliente","Fecha","Total","Estado");
        tbOrdenes=new JTable(tmOrdenes);
        JPanel p=new JPanel(new BorderLayout(8,8)); p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JScrollPane(tbOrdenes),BorderLayout.CENTER);
        JPanel botones=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton nueva=new JButton("Crear orden"), completar=new JButton("Completar orden"), cancelar=new JButton("Cancelar orden");
        nueva.addActionListener(e->crearOrden()); completar.addActionListener(e->cambiarEstado(true)); cancelar.addActionListener(e->cambiarEstado(false));
        botones.add(nueva); botones.add(completar); botones.add(cancelar); p.add(botones,BorderLayout.SOUTH);
        return p;
    }

    private String pedir(String mensaje) { return JOptionPane.showInputDialog(this,mensaje); }
    private double numero(String s,String campo) {
        try { double n=Double.parseDouble(s.trim()); if(n<0) throw new Exception(); return n; }
        catch(Exception e){ throw new IllegalArgumentException("Ingrese un valor numérico válido para "+campo+"."); }
    }
    private int entero(String s,String campo) {
        try { int n=Integer.parseInt(s.trim()); if(n<=0) throw new Exception(); return n; }
        catch(Exception e){ throw new IllegalArgumentException("Ingrese un entero mayor que cero para "+campo+"."); }
    }
    private void mensajeError(Exception e) { JOptionPane.showMessageDialog(this,e.getMessage(),"Aviso",JOptionPane.WARNING_MESSAGE); }

    private void registrarProducto() {
        try {
            String c=pedir("Código del producto:"); if(c==null)return;
            if(buscarProducto(c)!=null) throw new IllegalArgumentException("Ese código ya existe.");
            String n=pedir("Nombre:"); if(n==null)return;
            String d=pedir("Descripción:"); if(d==null)return;
            String m=pedir("Marca:"); if(m==null)return;
            double pr=numero(pedir("Precio:"),"precio");
            productos.add(new Producto(c,n,d,m,pr)); Persistencia.guardar("productos.dat",productos); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private void registrarCliente() {
        try {
            String dpi=pedir("DPI:"); if(dpi==null)return;
            if(clientes.stream().anyMatch(x->x.dpi.equals(dpi))) throw new IllegalArgumentException("El DPI ya está registrado.");
            String n=pedir("Nombre completo:"); if(n==null)return;
            String d=pedir("Dirección:"); if(d==null)return;
            String f=pedir("Fecha de nacimiento (AAAA-MM-DD):"); if(f==null)return;
            LocalDate fecha=LocalDate.parse(f);
            String tipo=(String)JOptionPane.showInputDialog(this,"Tipo de cliente:","Cliente",JOptionPane.QUESTION_MESSAGE,null,new String[]{"Regular","VIP"},"Regular");
            if(tipo==null)return;
            clientes.add(new Cliente(dpi,n,d,fecha,tipo)); Persistencia.guardar("clientes.dat",clientes); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private void registrarEmpleado() {
        try {
            String id=pedir("ID empleado:"); if(id==null)return;
            if(empleados.stream().anyMatch(x->x.idEmpleado.equals(id))) throw new IllegalArgumentException("El ID ya existe.");
            String n=pedir("Nombre:"); if(n==null)return;
            String dpi=pedir("DPI:"); if(dpi==null)return;
            String d=pedir("Dirección:"); if(d==null)return;
            String p=pedir("Puesto:"); if(p==null)return;
            double s=numero(pedir("Salario:"),"salario");
            empleados.add(new Empleado(id,n,dpi,d,p,s)); Persistencia.guardar("empleados.dat",empleados); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private void registrarBodega() {
        try {
            if(empleados.isEmpty()) throw new IllegalArgumentException("Primero registre un empleado.");
            String id=pedir("ID bodega:"); if(id==null)return;
            if(bodegas.stream().anyMatch(x->x.idBodega.equals(id))) throw new IllegalArgumentException("El ID ya existe.");
            Empleado emp=(Empleado)JOptionPane.showInputDialog(this,"Responsable:","Bodega",JOptionPane.QUESTION_MESSAGE,null,empleados.toArray(),empleados.get(0));
            if(emp==null)return;
            double cap=numero(pedir("Capacidad:"),"capacidad");
            String dir=pedir("Dirección:"); if(dir==null)return;
            bodegas.add(new Bodega(id,emp,cap,dir)); Persistencia.guardar("bodegas.dat",bodegas); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private void registrarStock() {
        try {
            if(productos.isEmpty()||bodegas.isEmpty()) throw new IllegalArgumentException("Registre al menos un producto y una bodega.");
            Producto prod=(Producto)JOptionPane.showInputDialog(this,"Producto:","Inventario",JOptionPane.QUESTION_MESSAGE,null,productos.toArray(),productos.get(0));
            if(prod==null)return;
            Bodega bod=(Bodega)JOptionPane.showInputDialog(this,"Bodega:","Inventario",JOptionPane.QUESTION_MESSAGE,null,bodegas.toArray(),bodegas.get(0));
            if(bod==null)return;
            int cant=entero(pedir("Cantidad que ingresará:"),"cantidad");
            inventario.agregarStock(prod,cant,bod); guardarInventario(); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private void crearOrden() {
        try {
            if(clientes.isEmpty()||productos.isEmpty()) throw new IllegalArgumentException("Registre clientes y productos antes de crear una orden.");
            Cliente cli=(Cliente)JOptionPane.showInputDialog(this,"Seleccione cliente:","Nueva orden",JOptionPane.QUESTION_MESSAGE,null,clientes.toArray(),clientes.get(0));
            if(cli==null)return;
            OrdenCompra o=new OrdenCompra("ORD-"+String.format("%03d",ordenes.size()+1),cli);
            boolean seguir=true;
            while(seguir) {
                Producto p=(Producto)JOptionPane.showInputDialog(this,"Seleccione producto:","Productos de la orden",JOptionPane.QUESTION_MESSAGE,null,productos.toArray(),productos.get(0));
                if(p==null) break;
                int cant=entero(pedir("Cantidad de "+p.nombre+":"),"cantidad");
                if(inventario.consultarStock(p)<cant) throw new IllegalArgumentException("Stock insuficiente. Disponible: "+inventario.consultarStock(p));
                o.agregarProducto(p,cant);
                seguir=JOptionPane.showConfirmDialog(this,"¿Agregar otro producto?","Orden",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
            }
            if(o.productos.isEmpty()) throw new IllegalArgumentException("La orden debe contener al menos un producto.");
            ordenes.add(o); Persistencia.guardar("ordenes.dat",ordenes); actualizarTodo();
            JOptionPane.showMessageDialog(this,"Orden creada como pendiente. Complete la orden para descontar el inventario.");
        } catch(Exception e){mensajeError(e);}
    }
    private void cambiarEstado(boolean completar) {
        int fila=tbOrdenes.getSelectedRow();
        if(fila<0){mensajeError(new IllegalArgumentException("Seleccione una orden."));return;}
        OrdenCompra o=ordenes.get(fila);
        try {
            if(!"Pendiente".equals(o.estado)) throw new IllegalArgumentException("Solo se pueden modificar órdenes pendientes.");
            if(completar) {
                // Validar todo antes de descontar para evitar actualizaciones parciales.
                Map<String,Integer> requeridos=new HashMap<>();
                Map<String,Producto> refs=new HashMap<>();
                for(DetalleOrden d:o.productos){ requeridos.merge(d.producto.codigo,d.cantidad,Integer::sum); refs.put(d.producto.codigo,d.producto); }
                for(String cod:requeridos.keySet())
                    if(inventario.consultarStock(refs.get(cod))<requeridos.get(cod)) throw new IllegalArgumentException("Stock insuficiente para "+refs.get(cod).nombre);
                for(String cod:requeridos.keySet()) inventario.descontarStock(refs.get(cod),requeridos.get(cod));
                o.estado="Completada"; guardarInventario();
            } else o.estado="Cancelada";
            Persistencia.guardar("ordenes.dat",ordenes); actualizarTodo();
        } catch(Exception e){mensajeError(e);}
    }
    private Producto buscarProducto(String codigo) { return productos.stream().filter(p->p.codigo.equalsIgnoreCase(codigo)).findFirst().orElse(null); }
    private void guardarInventario() { Persistencia.guardar("inventario.dat",new ArrayList<>(Arrays.asList(inventario))); }
    private void actualizarTodo() {
        if(tmProductos==null)return;
        tmProductos.setRowCount(0); for(Producto p:productos)tmProductos.addRow(new Object[]{p.codigo,p.nombre,p.descripcion,p.marca,String.format("Q %.2f",p.precio)});
        tmClientes.setRowCount(0); for(Cliente c:clientes)tmClientes.addRow(new Object[]{c.dpi,c.nombre,c.direccion,c.fechaNacimiento,c.tipo});
        tmEmpleados.setRowCount(0); for(Empleado e:empleados)tmEmpleados.addRow(new Object[]{e.idEmpleado,e.nombre,e.dpi,e.direccion,e.puesto,String.format("Q %.2f",e.salario)});
        tmBodegas.setRowCount(0); for(Bodega b:bodegas)tmBodegas.addRow(new Object[]{b.idBodega,b.empleado.nombre,b.capacidad,b.direccion});
        tmInventario.setRowCount(0); for(Producto p:productos) {
            int stock=inventario.consultarStock(p);
            StringBuilder bs=new StringBuilder();
            for(DetalleInventario d:inventario.existencias)if(d.producto.codigo.equals(p.codigo)) { if(bs.length()>0)bs.append(", "); bs.append(d.bodega.idBodega).append(": ").append(d.cantidad); }
            tmInventario.addRow(new Object[]{p.nombre,stock,bs.toString()});
        }
        tmOrdenes.setRowCount(0); for(OrdenCompra o:ordenes)tmOrdenes.addRow(new Object[]{o.codigo,o.cliente.nombre,o.fecha,String.format("Q %.2f",o.total),o.estado});
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new Main().setVisible(true));
    }
}
