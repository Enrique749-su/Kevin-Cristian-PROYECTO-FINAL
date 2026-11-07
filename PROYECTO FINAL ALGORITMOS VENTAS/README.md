# Sistema de Gestión de Ventas e Inventario

Aplicación de escritorio desarrollada en Java con Swing y Programación Orientada a Objetos.

## Requisitos
- JDK 17 o superior.
- No requiere librerías externas.

## Ejecutar desde consola
Abra una terminal en la carpeta `SistemaGestionVentasJava` y ejecute:

```bash
javac -encoding UTF-8 -d out src/gestion/*.java
java -cp out gestion.Main
```

En Windows también puede ejecutar `ejecutar.bat`.

## Módulos
- Productos
- Clientes
- Empleados
- Bodegas
- Inventario
- Órdenes de compra

## Persistencia
Los registros se guardan como archivos serializados en la carpeta `datos/`. No elimine esos archivos si desea conservar la información.

## Flujo de venta
1. Registre productos, clientes, empleados y bodegas.
2. Ingrese existencias desde el módulo Inventario.
3. Cree una orden con uno o más productos.
4. Seleccione la orden y pulse **Completar orden** para descontar el stock.
5. Si cancela la orden, el inventario no se modifica.

## GitHub
```bash
git init
git add .
git commit -m "Version inicial del sistema de gestion"
git branch -M main
git remote add origin https://github.com/Enrique749-su/Kevin-Cristian-PROYECTO-FINAL
git push -u origin main
```
