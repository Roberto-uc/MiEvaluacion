import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

abstract class HolaMundo {
    protected String mensaje;

    public HolaMundo(String msj) {
        setMensaje(msj);
    }

    public void setMensaje(String msj) {
        this.mensaje = (msj == null || msj.isEmpty()) ? "MENSAJE" : msj;
    }

    public abstract void showMensaje();
}

class HMConsola extends HolaMundo {
    private String color;

    public static final String RED = "\u001B[38;5;196m";
    public static final String BLUE = "\u001B[38;5;33m";
    public static final String GREEN = "\u001B[38;5;46m";
    public static final String YELLOW = "\u001B[38;5;220m";
    public static final String PURPLE = "\u001B[38;5;129m";
    public static final String CYAN = "\u001B[38;5;51m";
    public static final String WHITE = "\u001B[38;5;15m";
    public static final String RESET = "\u001B[0m";

    public HMConsola(String msj, String color) {
        super(msj);
        setColor(color);
    }

    public HMConsola() {
        super("Hola Mundo");
        setColor("blue");
    }

    public void setColor(String color) {
        switch (color.toLowerCase()) {
            case "red":
                this.color = RED;
                break;
            case "blue":
                this.color = BLUE;
                break;
            case "green":
                this.color = GREEN;
                break;
            case "yellow":
                this.color = YELLOW;
                break;
            case "purple":
                this.color = PURPLE;
                break;
            case "cyan":
                this.color = CYAN;
                break;
            case "white":
                this.color = WHITE;
                break;
            default:
                this.color = RESET;
                break;
        }
    }

    @Override
    public void showMensaje() {
        System.out.println(color + mensaje + RESET);
    }
}

class HMVentana extends HolaMundo {
    private JFrame ventana;

    public HMVentana(String msj) {
        super(msj);
    }

    public HMVentana() {
        super("HOLA MUNDO, SOY HIJO DE VENTANA");
    }

    @Override
    public void showMensaje() {
        ventana = new JFrame("Mensaje");
        JLabel label = new JLabel(mensaje, JLabel.CENTER);
        ventana.add(label);
        ventana.setSize(300, 150);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> coloresValidos = Arrays.asList("red", "blue", "green", "yellow", "purple", "cyan", "white");

        try {
            String destino = "";
            while (true) {
                System.out.print("¿Dónde quieres mostrar el mensaje? (consola/ventana): ");
                destino = br.readLine().trim().toLowerCase();
                if (destino.equals("consola") || destino.equals("ventana")) {
                    break;
                }
                System.out.println("Opción no válida. Usa 'consola' o 'ventana'.");
            }

            System.out.print("Escribe tu mensaje: ");
            String mensaje = br.readLine();

            if (destino.equals("consola")) {
                String color = "";
                while (true) {
                    System.out.print("Color (red, blue, green, yellow, purple, cyan, white): ");
                    color = br.readLine().trim().toLowerCase();
                    if (coloresValidos.contains(color)) {
                        break;
                    }
                    System.out.println("Color inválido. Por favor ingresa un color válido.");
                }
                HMConsola consola = new HMConsola(mensaje, color);
                consola.showMensaje();

            } else if (destino.equals("ventana")) {
                HMVentana ventana = new HMVentana(mensaje);
                ventana.showMensaje();
            }

        } catch (IOException e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        }
    }
}
