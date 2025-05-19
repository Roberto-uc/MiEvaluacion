import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

class ValidacionException extends Exception {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}

enum Urgencia {
    BAJA(1, "\u001B[32m"),  // Verde
    MEDIA(2, "\u001B[33m"), // Amarillo
    ALTA(3, "\u001B[31m");  // Rojo

    private final int nivel;
    private final String color;

    Urgencia(int nivel, String color) {
        this.nivel = nivel;
        this.color = color;
    }

    public static Urgencia fromNivel(int nivel) {
        for (Urgencia u : values()) {
            if (u.nivel == nivel) {
                return u;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getColor() {
        return color;
    }
}

class Nota {
    private static int contador = 1;
    private final int id;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String contenido;
    private Urgencia urgencia;

     private static final DateTimeFormatter formatoFecha = 
        DateTimeFormatter.ofPattern("dd/MMM/yyyy", new Locale("es", "ES"));

    public Nota(String contenido, Urgencia urgencia) throws ValidacionException {
        this.id = contador++;
        this.fechaCreacion = LocalDateTime.now();
        setContenido(contenido);
        setUrgencia(urgencia);
    }

    public void setContenido(String contenido) throws ValidacionException {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new ValidacionException("El contenido no puede estar vacío");
        }
        this.contenido = contenido;
        actualizarFecha();
    }

    public void setUrgencia(Urgencia urgencia) throws ValidacionException {
        if (urgencia == null) {
            throw new ValidacionException("La urgencia no puede ser nula");
        }
        this.urgencia = urgencia;
        actualizarFecha();
    }

    public void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String toArchivo() {
        DateTimeFormatter dtfPersonalizado = DateTimeFormatter.ofPattern("d - MMMM - yyyy", new Locale("es", "ES"));
        String colorTexto = switch (urgencia) {
            case BAJA -> "verde";
            case MEDIA -> "amarillo";
            case ALTA -> "rojo";    
        };

        return String.join("\n",
            "Num: " + id,
            "Fecha: " + fechaCreacion.format(dtfPersonalizado),
            "Texto: " + contenido,
            "Urgencia: " + urgencia.name().charAt(0) + urgencia.name().substring(1).toLowerCase(),
            "color : " + colorTexto
        );
    }

    @Override
    public String toString() {
        return urgencia.getColor() +
               "ID: " + id + "\n" +
               "Fecha: " + fechaCreacion.format(formatoFecha) + "\n" +
               "Urgencia: " + urgencia.name().toLowerCase() + "\n" +
               "Contenido: " + contenido + "\n" +
               "\u001B[0m";
    }

    public static void actualizarContador(int maxId) {
        contador = Math.max(contador, maxId + 1);
    }

    public int getId() { return id; }
    
}

class BlockdeNotas{

    private static final String DIRECTORIO = "notas";
    private final List<Nota> notas = new ArrayList<>();

    public BlockdeNotas(){
        crearDirectorio();
    }

    private void crearDirectorio() {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO));
        } catch (IOException e) {
            System.err.println("Error creando directorio: " + e.getMessage());
        }
    }

    public void agregarNota(Nota nota){
        notas.add(nota);
    }

    public Nota buscarNota(int id) {
        return notas.stream()
                .filter(nota -> nota.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean eliminarNota(int id) {
        Iterator<Nota> it = notas.iterator();
        while (it.hasNext()) {
            Nota nota = it.next();
            if (nota.getId() == id) {
                it.remove();
                eliminarArchivo(id);
                return true;
            }
        }
        return false;
    }

    private void eliminarArchivo(int id) {
        try {
            Path archivo = Paths.get(DIRECTORIO + File.separator + id + ".txt");
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            System.err.println("Error eliminando archivo: " + e.getMessage());
        }
    }
    
    public void guardarNotas() {
        for (Nota nota : notas) {
            try {
                Path archivo = Paths.get(DIRECTORIO + File.separator + nota.getId() + ".txt");
                Files.writeString(
                archivo,
                nota.toArchivo(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            } catch (IOException e) {
            System.err.println("Error guardando nota ID " + nota.getId() + ": " + e.getMessage());
        }
        }
    }

    public void cargarNotas() throws IOException {
        notas.clear();
        int maxId = 0;

        try (Stream<Path> stream = Files.list(Paths.get(DIRECTORIO))) {
            List<Path> archivos = stream
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".txt"))
                .toList();

            for (Path archivo : archivos) {
                try {
                    Map<String, String> datos = new HashMap<>();
                    Files.readAllLines(archivo).forEach(linea -> {
                        String[] partes = linea.split(":", 2);
                        if (partes.length == 2) {
                            datos.put(partes[0].trim(), partes[1].trim());
                        }
                    });

                    Nota nota = new Nota(
                        datos.get("Texto"),
                        Urgencia.valueOf(datos.get("Urgencia").toUpperCase())
                    );

                    maxId = Math.max(maxId, nota.getId());
                    notas.add(nota);
                } catch (Exception e) {
                    System.err.println("Error cargando archivo: " + archivo.getFileName() + " - " + e.getMessage());
                }
            }
        }
        Nota.actualizarContador(maxId);
    }
    
    public void mostrarNotas(int cantidad) {
        if (notas.isEmpty()) {
            System.out.println("No hay notas disponibles");
            return;
        }

        notas.stream()
            .sorted(Comparator.comparing(Nota::getId).reversed())
            .limit(cantidad)
            .forEach(System.out::println);
    }

    public boolean actualizarNota(int id, String nuevoContenido, Urgencia nuevaUrgencia) throws ValidacionException {
        Nota nota = buscarNota(id);
        if (nota != null) {
            nota.setContenido(nuevoContenido);
            nota.setUrgencia(nuevaUrgencia);
            return true;
        }
        return false;
    }
    
}


public class MainNotas{
    
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final BlockdeNotas bloc = new BlockdeNotas();
    
    public static void main(String [] args){
         cargarDatos();
         mostrarMenu();
    }

    private static void cargarDatos() {
        try {
            bloc.cargarNotas();
        } catch (IOException e) {
            System.out.println("No se encontraron notas previas");
        }
    }

    private static void mostrarMenu() {
        while (true) {
            System.out.println("\n=== BLOC DE NOTAS ===");
            System.out.println("1. Nueva nota");
            System.out.println("2. Mostrar últimas notas");
            System.out.println("3. Buscar nota por ID");
            System.out.println("4. Eliminar nota");
            System.out.println("5. Actualizar nota");
            System.out.println("6. Salir");
            System.out.print("Seleccione opción: ");

            try {
                int opcion = Integer.parseInt(reader.readLine());

                switch (opcion) {
                    case 1 -> crearNota();
                    case 2 -> mostrarUltimas();
                    case 3 -> buscarNota();
                    case 4 -> eliminarNota();
                    case 5 -> actualizarNota();
                    case 6 -> { guardarYSalir(); return; }
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido");
            } catch (IOException | ValidacionException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void crearNota() throws IOException, ValidacionException {
        System.out.print("Contenido de la nota: ");
        String contenido = reader.readLine();

        Urgencia urgencia = seleccionarUrgencia();

        bloc.agregarNota(new Nota(contenido, urgencia));
        System.out.println("Nota creada exitosamente!");
    }

    private static Urgencia seleccionarUrgencia() throws IOException {
        while (true) {
            try {
                System.out.println("\nNivel de urgencia:");
                System.out.println("1. Baja");
                System.out.println("2. Media");
                System.out.println("3. Alta");
                System.out.print("Seleccione (1-3): ");

                int opcion = Integer.parseInt(reader.readLine());
                return Urgencia.fromNivel(opcion);

            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Opción inválida");
            }
        }
    }

    private static void mostrarUltimas() throws IOException {
        System.out.print("Cantidad de notas a mostrar: ");
        int cantidad = Integer.parseInt(reader.readLine());
        bloc.mostrarNotas(cantidad);
    }

    private static void buscarNota() throws IOException {
        System.out.print("ID de la nota: ");
        int id = Integer.parseInt(reader.readLine());
        Nota nota = bloc.buscarNota(id);

        if (nota != null) {
            System.out.println(nota);
        } else {
            System.out.println("No se encontró la nota");
        }
    }
    
    private static void eliminarNota() throws IOException {
        System.out.print("ID de la nota a eliminar: ");
        int id = Integer.parseInt(reader.readLine());

        if (bloc.eliminarNota(id)) {
            System.out.println("Nota eliminada exitosamente");
        } else {
            System.out.println("No se encontró la nota");
        }
    }

    private static void actualizarNota() throws IOException, ValidacionException {
        System.out.print("ID de la nota a actualizar: ");
        int id = Integer.parseInt(reader.readLine());

        System.out.print("Nuevo contenido: ");
        String nuevoContenido = reader.readLine();

        Urgencia nuevaUrgencia = seleccionarUrgencia();

        if (bloc.actualizarNota(id, nuevoContenido, nuevaUrgencia)) {
            System.out.println("Nota actualizada exitosamente");
        } else {
            System.out.println("No se encontró la nota");
        }
    }

    private static void guardarYSalir() {
        bloc.guardarNotas();
        System.out.println("Datos guardados correctamente");
    }


} 