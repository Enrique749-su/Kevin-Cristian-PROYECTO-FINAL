package gestion;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Persistencia {
    private static final Path DIR = Paths.get("datos");
    static { try { Files.createDirectories(DIR); } catch(IOException e) { throw new RuntimeException(e); } }

    @SuppressWarnings("unchecked")
    public static <T> List<T> cargar(String archivo) {
        Path p=DIR.resolve(archivo);
        if (!Files.exists(p)) return new ArrayList<>();
        try (ObjectInputStream in=new ObjectInputStream(new FileInputStream(p.toFile()))) {
            return (List<T>) in.readObject();
        } catch(Exception e) {
            System.err.println("No se pudo leer " + archivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public static void guardar(String archivo, Object objeto) {
        try (ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(DIR.resolve(archivo).toFile()))) {
            out.writeObject(objeto);
        } catch(IOException e) { throw new RuntimeException("Error al guardar " + archivo + ": " + e.getMessage(), e); }
    }
}
