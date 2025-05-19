import java.io.*;
import java.util.*;

abstract class Cliente {
    
    //Atributos
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;

    //Constructor
    public Cliente() {
        setNombre("Usuario General");
        setDireccion("Av. Montejo");
        setTelefono("9961070707");
        setEmail("usuariogeneral@gmail.com");
    }

    public Cliente(String nombre) {
        setNombre(nombre);
        setDireccion("Av, Benito Juarez");
    }

    //Metodos setters y getters
    public void setNombre(String nombre) {
        this.nombre = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : "Usuario General";
    }

    public void setDireccion(String direccion) {
        this.direccion = (direccion != null && !direccion.trim().isEmpty()) ? direccion.trim() : "Av. Montejo";
    }

    public void setTelefono(String telefono) {
        this.telefono = (telefono != null && !telefono.trim().isEmpty()) ? telefono.trim() : "9984567890";
    }

    public void setEmail(String email) {
        this.email = (email != null && !email.trim().isEmpty()) ? email.trim() : "usuriogeneral@itescam.com";
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
    //Metodo para calcular el total
    public abstract double calcularTotal(Pedido pedido);

    //Metodo ToString
    public String toString() {
        return "Nombre: " + getNombre() + "\nDireccion: " + getDireccion() + "\nTelefono: " + getTelefono() + "\nEmail: " + getEmail();
    }
}

class ClienteFrecuente extends Cliente {
    public ClienteFrecuente(String nombre){
        super(nombre);
    }

    public double calcularTotal(Pedido pedido){
        double subtotal = pedido.calcularSubtotal();
        double descuento = subtotal * 0.5;
        double total = subtotal - descuento;
        double propina = subtotal * 0.10;
        return total + propina;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nTipo: VIP";
    }
}

class ClienteRegular extends Cliente {
    public ClienteRegular(String nombre) {
        super(nombre);
    }

    @Override
    public double calcularTotal(Pedido pedido) {
        double subtotal = pedido.calcularSubtotal();
        double propina = subtotal * 0.10;
        return subtotal + propina;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTipo: Regular";
    }
}

class ClienteVip extends Cliente{
    public ClienteVip(String nombre){
        super(nombre);
    }

    
    @Override
    public double calcularTotal(Pedido pedido){
        double subtotal = pedido.calcularSubtotal();
        double descuento = subtotal * 0.10;
        double total = subtotal - descuento;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("¿Desea dejar propina...? (si/no)");
            String respuesta = reader.readLine().trim().toLowerCase();

            if (respuesta.equals("si")) {
                double propina = total * 0.10;
                total += propina;
            }
        } catch (IOException e) {
            System.out.println("Error el dejar propina.");
        }
        return total;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nTipo: VIP";
    }
}

class Platillo {
    
    private int id;
    private String nombre;
    private double precio;

    public Platillo(String nombre) {
        setId();
        setNombre(nombre);
        setPrecio(0);
    }

    public Platillo(String nombre, double precio) {
        setId();
        setNombre(nombre);
        setPrecio(precio);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setId() {
        this.id = (int) (Math.random() * 1000);
    }

    public void setNombre(String nombre) {
        this.nombre = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : "Desconocido";
    }

    public void setPrecio(double precio) {
        this.precio = (precio > 0) ? precio : 0;
    }

    public String toString() {
        return "ID: " + getId() + "\nNombre: " + getNombre() + "\nPrecio: " + getPrecio();
    }
}

class Pedido {
    private Cliente cliente;
    private List<Platillo> platillos;
    private double total;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.platillos = new ArrayList<>();

    }

    public void agregarPlatillo(Platillo platillo) {
        platillos.add(platillo);
    }
    public double calcularSubtotal(){
        double subtotal = 0;
        for(Platillo platillo : platillos){
            subtotal += platillo.getPrecio();
        }
        return subtotal;
    }
    public void calcularYGuardarTotal() {
        this.total = cliente.calcularTotal(this);
    }

    public double getTotal() {
        return total;
    }
    public Cliente getCliente(){
        return cliente;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido de ").append(cliente.getNombre()).append("\n");
        for (Platillo platillo : platillos) {
            sb.append(platillo.getNombre()).append(":$").append(platillo.getPrecio()).append("\n");
        }
        sb.append("Total:$").append(getTotal());
        return sb.toString();
    }

    public void guardarPedidoEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pedidos_clientes.txt", true))) {
            writer.write(this.toString());
            writer.newLine();
            writer.write("--------------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class PedidoCorporativo extends Cliente{
    private static final double MONTO_MINIMO = 400.00;

    public PedidoCorporativo(String nombre){
        super(nombre);
    }

    public double calcularTotal(Pedido pedido){
        double subtotal = pedido.calcularSubtotal();
        if(subtotal >= MONTO_MINIMO){
            subtotal *= 0.85;

        }
        return subtotal;
    }
     @Override
    public String toString() {
        return super.toString() + "\nTipo: Corporativo";
    }
}

class Restaurante {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void realizarPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.guardarPedidoEnArchivo();
    }

    public void generarResumenVentas() {
        System.out.println("Resumen de Ventas:");
        for (Pedido p : pedidos) {
            System.out.println(p);
            System.out.println("--------------");
        }
    }

    public void consultarHistorial() {
        System.out.println("Historial de Clientes:");
        for (Cliente c : clientes) {
            System.out.println(c);
            System.out.println("--------------");
        }
    }
    public Cliente buscarClientePorNombre(String nombre) {
    for (Cliente c : clientes) {
        if (c.getNombre().equalsIgnoreCase(nombre)) {
            return c;
        }
    }
    return null;
    }
}


public class RestauranteMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Restaurante restaurante = new Restaurante();

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Realizar Pedido");
            System.out.println("3. Gestionar Ventas");
            System.out.println("4. Ver Historial de Clientes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion(reader, 1, 5);

            switch (opcion) {
                case 1:
                    registrarCliente(reader, restaurante);
                    break;
                case 2:
                    realizarPedido(reader, restaurante);
                    break;
                case 3:
                    restaurante.generarResumenVentas();
                    break;
                case 4:
                    restaurante.consultarHistorial();
                    break;
                case 5:
                    salir = true;
                    System.out.println("Gracias por usar el sistema. ¡Hasta luego!");
                    break;
            }
        }
    }

    private static int leerOpcion(BufferedReader reader, int min, int max) {
        int opcion = -1;
        while (true) {
            try {
                opcion = Integer.parseInt(reader.readLine());
                if (opcion < min || opcion > max) {
                    throw new IllegalArgumentException("La opción debe estar entre " + min + " y " + max);
                }
                return opcion;
            } catch (IOException | IllegalArgumentException e) {
                System.out.print("Entrada inválida. Intente de nuevo: ");
            }
        }
    }

    private static void registrarCliente(BufferedReader reader, Restaurante restaurante) throws IOException {
        System.out.println("\n=== Registrar Cliente ===");

        System.out.print("Nombre: ");
        String nombre = reader.readLine();

        System.out.print("Dirección: ");
        String direccion = reader.readLine();

        System.out.print("Teléfono: ");
        String telefono = reader.readLine();

        System.out.print("Email: ");
        String email = reader.readLine();

        System.out.println("Tipo de cliente:");
        System.out.println("1. Regular");
        System.out.println("2. Frecuente");
        System.out.println("3. VIP");
        System.out.println("4. Corporativo");
        System.out.print("Seleccione una opción: ");
        int tipo = Integer.parseInt(reader.readLine());

        Cliente cliente;
        switch (tipo) {
            case 2:
                cliente = new ClienteFrecuente(nombre);
                break;
            case 3:
                cliente = new ClienteVip(nombre);
                break;
            case 4:
                cliente = new PedidoCorporativo(nombre);
                break;
            default:
                cliente = new ClienteRegular(nombre);
                break;
        }

        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);

        restaurante.registrarCliente(cliente);
        System.out.println("Cliente registrado exitosamente.");
    }

    private static void realizarPedido(BufferedReader reader, Restaurante restaurante) throws IOException {
        System.out.println("\n=== Realizar Pedido ===");

        System.out.print("Ingrese nombre del cliente (dejar vacío para 'Usuario General'): ");
        String nombreCliente = reader.readLine().trim();

        Cliente cliente;
        if (nombreCliente.isEmpty()) {
            cliente = new ClienteRegular("Usuario General");
        } else {
            cliente = restaurante.buscarClientePorNombre(nombreCliente);
            if (cliente == null) {
                System.out.println("Cliente no registrado. Se usará Cliente Regular por defecto.");
                cliente = new ClienteRegular(nombreCliente);
            }
        }

        Pedido pedido = new Pedido(cliente);

        boolean agregarMas = true;
        while (agregarMas) {
            try {
                System.out.print("Nombre del platillo: ");
                String nombrePlatillo = reader.readLine();

                System.out.print("Precio del platillo: ");
                double precio = Double.parseDouble(reader.readLine());

                Platillo platillo = new Platillo(nombrePlatillo, precio);
                pedido.agregarPlatillo(platillo);

                System.out.print("¿Desea agregar otro platillo? (s/n): ");
                String respuesta = reader.readLine().trim().toLowerCase();
                agregarMas = respuesta.equals("s");
            } catch (NumberFormatException e) {
                System.out.println("Error: El precio debe ser un número válido. Intente agregar el platillo nuevamente.");
            }
        }

        // 🔒 Total se calcula una sola vez y se guarda
        pedido.calcularYGuardarTotal();

        restaurante.realizarPedido(pedido);
        System.out.println("Pedido registrado con éxito.");
    }
}


