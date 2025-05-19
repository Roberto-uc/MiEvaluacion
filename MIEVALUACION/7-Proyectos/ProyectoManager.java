import java.io.*;
import java.util.*;

// Interfaz para guardar proyectos en archivo
interface SerializableProyecto {
    void guardarEnArchivo() throws IOException;
}

// Excepción personalizada para los datos no válidos
class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}

// Clase base Proyecto
abstract class Proyecto implements SerializableProyecto {
    private String titulo;
    private String investigadorPrincipal;
    private int duracionMeses;

    public Proyecto(String titulo, String investigadorPrincipal, int duracionMeses) throws InvalidDataException {
        setTitulo(titulo);
        setInvestigadorPrincipal(investigadorPrincipal);
        setDuracionMeses(duracionMeses);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws InvalidDataException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new InvalidDataException("El título no puede estar vacío.");
        }
        this.titulo = titulo;
    }

    public String getInvestigadorPrincipal() {
        return investigadorPrincipal;
    }

    public void setInvestigadorPrincipal(String investigadorPrincipal) throws InvalidDataException {
        if (investigadorPrincipal == null || investigadorPrincipal.trim().isEmpty()) {
            throw new InvalidDataException("El investigador principal no puede estar vacío.");
        }
        this.investigadorPrincipal = investigadorPrincipal;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) throws InvalidDataException {
        if (duracionMeses <= 0) {
            throw new InvalidDataException("La duración debe ser un número positivo de meses.");
        }
        this.duracionMeses = duracionMeses;
    }

    public void mostrarInfo() {
        System.out.println("Título: " + titulo);
        System.out.println("Investigador Principal: " + investigadorPrincipal);
        System.out.println("Duración (meses): " + duracionMeses);
    }
}

// Proyecto Teórico
class ProyectoTeorico extends Proyecto {
    private String campoEstudio;

    public ProyectoTeorico(String titulo, String investigadorPrincipal, int duracionMeses, String campoEstudio)
            throws InvalidDataException {
        super(titulo, investigadorPrincipal, duracionMeses);
        setCampoEstudio(campoEstudio);
    }

    public String getCampoEstudio() {
        return campoEstudio;
    }

    public void setCampoEstudio(String campoEstudio) throws InvalidDataException {
        if (campoEstudio == null || campoEstudio.trim().isEmpty()) {
            throw new InvalidDataException("El campo de estudio no puede estar vacío.");
        }
        this.campoEstudio = campoEstudio;
    }

    @Override
    public void guardarEnArchivo() throws IOException {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("proyectos.txt", true))) {
            writer.write("TEORICO;" + getTitulo() + ";" + getInvestigadorPrincipal() + ";"
                    + getDuracionMeses() + ";" + campoEstudio);
            writer.newLine();
        }
    }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Campo de Estudio: " + campoEstudio);
    }
}

// Proyecto Experimental
class ProyectoExperimental extends Proyecto {
    private String laboratorioAsignado;

    public ProyectoExperimental(String titulo, String investigadorPrincipal, int duracionMeses,
            String laboratorioAsignado) throws InvalidDataException {
        super(titulo, investigadorPrincipal, duracionMeses);
        setLaboratorioAsignado(laboratorioAsignado);
    }

    public String getLaboratorioAsignado() {
        return laboratorioAsignado;
    }

    public void setLaboratorioAsignado(String laboratorioAsignado) throws InvalidDataException {
        if (laboratorioAsignado == null || laboratorioAsignado.trim().isEmpty()) {
            throw new InvalidDataException("El laboratorio asignado no puede estar vacío.");
        }
        this.laboratorioAsignado = laboratorioAsignado;
    }

    @Override
    public void guardarEnArchivo() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("proyectos.txt", true))) {
            writer.write("EXPERIMENTAL;" + getTitulo() + ";" + getInvestigadorPrincipal() + ";"
                    + getDuracionMeses() + ";" + laboratorioAsignado);
            writer.newLine();
        }
    }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Laboratorio Asignado: " + laboratorioAsignado);
    }
}

// Clase principal con menú y BufferReader
public class ProyectoManager {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        List<Proyecto> proyectos = new ArrayList<>();
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n--- Menú de Gestión de Proyectos ---");
                System.out.println("1. Crear Proyecto Teórico");
                System.out.println("2. Crear Proyecto Experimental");
                System.out.println("3. Mostrar Proyectos");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = Integer.parseInt(br.readLine());
                switch (opcion) {
                    case 1:
                        proyectos.add(crearProyectoTeorico());
                        break;
                    case 2:
                        proyectos.add(crearProyectoExperimental());
                        break;
                    case 3:
                        mostrarTodos(proyectos);
                        break;
                    case 4:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InvalidDataException e) {
                System.err.println("Error de validación: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error al leer/escribir archivo: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Entrada no es un número válido.");
            }
        }
        System.out.println("Programa finalizado.");
    }

    private static ProyectoTeorico crearProyectoTeorico() throws IOException, InvalidDataException {
        System.out.print("Título: ");
        String titulo = br.readLine();
        System.out.print("Investigador Principal: ");
        String investigador = br.readLine();
        System.out.print("Duración (meses): ");
        int duracion = Integer.parseInt(br.readLine());
        System.out.print("Campo de Estudio: ");
        String campo = br.readLine();

        ProyectoTeorico pt = new ProyectoTeorico(titulo, investigador, duracion, campo);
        pt.guardarEnArchivo();
        System.out.println("Proyecto Teórico guardado correctamente.");
        return pt;
    }

    private static ProyectoExperimental crearProyectoExperimental() throws IOException, InvalidDataException {
        System.out.print("Título: ");
        String titulo = br.readLine();
        System.out.print("Investigador Principal: ");
        String investigador = br.readLine();
        System.out.print("Duración (meses): ");
        int duracion = Integer.parseInt(br.readLine());
        System.out.print("Laboratorio Asignado: ");
        String laboratorio = br.readLine();

        ProyectoExperimental pe = new ProyectoExperimental(titulo, investigador, duracion, laboratorio);
        pe.guardarEnArchivo();
        System.out.println("Proyecto Experimental guardado correctamente.");
        return pe;
    }

    private static void mostrarTodos(List<Proyecto> proyectos) {
        if (proyectos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
            return;
        }
        for (Proyecto p : proyectos) {
            System.out.println("\n-----------------------------");
            p.mostrarInfo();
        }
    }
}