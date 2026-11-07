# Manual técnico

## Arquitectura
El sistema se organiza en el paquete `gestion`. Las clases del modelo representan Producto, Cliente, Empleado, Bodega, Inventario y OrdenCompra. `Persistencia` centraliza la lectura y escritura de archivos. `Main` construye la interfaz Swing.

## POO aplicada
- **Encapsulamiento / organización por clases:** cada entidad tiene su propia clase y datos relacionados.
- **Abstracción:** las entidades representan conceptos del negocio.
- **Composición:** una orden contiene detalles de productos; el inventario contiene detalles de existencias.
- **Asociación:** cliente-orden, bodega-empleado y producto-existencia.

## Persistencia
Se utiliza serialización de objetos (`ObjectInputStream` y `ObjectOutputStream`) en archivos `.dat` dentro de `datos/`. Los modelos implementan `Serializable`.

## Regla de actualización de stock
Al completar una orden, el sistema agrupa las cantidades por código de producto, valida primero que exista stock suficiente para todos los productos y después descuenta las cantidades. Una orden cancelada no altera el inventario.

## Compilación
```bash
javac -encoding UTF-8 -d out src/gestion/*.java
java -cp out gestion.Main
```

## Consideraciones
Esta versión es una base académica local. No incluye autenticación, roles, base de datos multiusuario ni edición/eliminación de registros. Para uso comercial real se recomienda agregar transacciones, respaldos, validaciones reforzadas y pruebas automatizadas.
