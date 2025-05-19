import java.io.*;
import java.util.*;

class Cliente {
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;

    public Cliente() {
        this("Desconocido", "Desconocido", "Desconocido", "Desconocido");
    }

    public Cliente(String nombre) {
        this(nombre, "Desconocido", "Desconocido", "Desconocido");
    }

    public Cliente(String nombre, String direccion, String telefono, String email) {
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
        setEmail(email);
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) this.nombre = "Desconocido";
        else this.nombre = nombre;
    }
    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) this.direccion = "Desconocido";
        else this.direccion = direccion;
    }
    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("\\d{7,15}")) this.telefono = "Desconocido";
        else this.telefono = telefono;
    }
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) this.email = "Desconocido";
        else this.email = email;
    }

    public float calcularTotalPedido(Pedido p) {
        return p.calcularTotal();
    }

    @Override
    public String toString() {
        return String.format("Cliente{nombre='%s', direccion='%s', telefono='%s', email='%s'}",
                nombre, direccion, telefono, email);
    }
}

class ClienteRegular extends Cliente {
    public ClienteRegular(String nombre, String telefono, String email) {
        super(nombre, "Desconocido", telefono, email);
    }
    @Override public float calcularTotalPedido(Pedido p) {
        return super.calcularTotalPedido(p) + 20f;
    }
    @Override public String toString() {
        return super.toString().replaceFirst("Cliente", "ClienteRegular");
    }
}

class ClienteFrecuente extends Cliente {
    public ClienteFrecuente(String nombre, String telefono, String email) {
        super(nombre, "Desconocido", telefono, email);
    }
    @Override public float calcularTotalPedido(Pedido p) {
        return super.calcularTotalPedido(p) * 0.95f + 20f;
    }
    @Override public String toString() {
        return super.toString().replaceFirst("Cliente", "ClienteFrecuente");
    }
}

class ClienteMayorista extends Cliente {
    public ClienteMayorista(String nombre, String telefono, String email) {
        super(nombre, "Desconocido", telefono, email);
    }
    
      @Override
    public float calcularTotalPedido(Pedido p) {
        float total = super.calcularTotalPedido(p);

        float descuento = total * 0.85f; // Aplica un 15% de descuento
        float envio = 20.0f; // Costo de envío

        if (descuento > 1000.0f) {
        envio = 0.0f; 

    }

    return descuento + envio;
}

    
    @Override public String toString() {
        return super.toString().replaceFirst("Cliente", "ClienteMayorista");
    }
}

class ClienteVip extends Cliente {
    public ClienteVip(String nombre, String telefono, String email) {
        super(nombre, "Desconocido", telefono, email);
    }
    @Override public float calcularTotalPedido(Pedido p) {
        return super.calcularTotalPedido(p) * 0.9f;
    }
    @Override public String toString() {
        return super.toString().replaceFirst("Cliente", "ClienteVip");
    }
}

class Producto {
    private static int counter;
    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        id = ++counter;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { if (stock >= 0) this.stock = stock; }

    @Override
    public String toString() {
        return String.format("%d) %s - Precio: $%.2f - Stock: %d", id, nombre, precio, stock);
    }
}

class Pedido {
    private static int counter;
    private final int id;
    private List<Producto> productos = new ArrayList<>();
    private float total;

    public Pedido() {
        id = ++counter;
    }

    public void agregarProducto(Producto p, int cantidad) {
        if (p.getStock() < cantidad) {
            System.out.println("Stock insuficiente para " + p.getNombre());
        } else {
            p.setStock(p.getStock() - cantidad);
            productos.add(p);
            total += p.getPrecio() * cantidad;
            System.out.println(cantidad + " x " + p.getNombre() + " agregado(s).");
        }
    }

    public float calcularTotal() {
        return total;
    }

    @Override
    public String toString() {
        return String.format("Pedido{id=%d, total=%.2f}", id, total);
    }
}

class Tienda {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    public void registrarCliente(Cliente c) {
        clientes.add(c);
        System.out.println("Cliente registrado: " + c.getNombre());
    }
    public List<Cliente> getClientes() {
        return clientes;
    }
    public void realizarPedido(Pedido p) {
        pedidos.add(p);
        System.out.println("Pedido registrado exitosamente. Total: $" + p.calcularTotal());
    }
    public void generarResumenDeVentas() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay ventas registradas.");
        } else {
            float totalVentas = 0;
            System.out.println("\nRESUMEN DE VENTAS:");
            for (Pedido p : pedidos) {
                System.out.println(p);
                totalVentas += p.calcularTotal();
            }
            System.out.println("Total ventas: $" + totalVentas + "\n");
        }
    }
    public void consultarHistorial() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos en el historial.");
        } else {
            System.out.println("\nHISTORIAL DE PEDIDOS:");
            for (Pedido p : pedidos) {
                System.out.println(p);
            }
            System.out.println();
        }
    }
}

public class EshopMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Tienda tienda = new Tienda();

        // Pre-carga de clientes
        tienda.registrarCliente(new ClienteRegular("Juan Pérez", "1234567", "juan@example.com"));
        tienda.registrarCliente(new ClienteFrecuente("Ana Gómez", "2345678", "ana@example.com"));
        tienda.registrarCliente(new ClienteMayorista("Empresa XYZ", "3456789", "contacto@xyz.com"));
        tienda.registrarCliente(new ClienteVip("Luisa López", "4567890", "luisa@vip.com"));

        // Pre-carga de productos
        List<Producto> catalogo = Arrays.asList(
            new Producto("Laptop", 1500.0, 10),
            new Producto("Mouse", 25.0, 50),
            new Producto("Teclado", 45.0, 30),
            new Producto("Monitor", 200.0, 20)
        );

        while (true) {
            System.out.println("\n--- BIENVENIDO A LA TIENDA ---");
            System.out.println("1) Registrar nuevo cliente");
            System.out.println("2) Realizar compra");
            System.out.println("3) Resumen de ventas");
            System.out.println("4) Historial de pedidos");
            System.out.println("0) Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(reader.readLine());

            switch (opcion) {
                case 1:
                    System.out.println("Seleccione tipo de cliente:");
                    System.out.println("1) Regular");
                    System.out.println("2) Frecuente");
                    System.out.println("3) Mayorista");
                    System.out.println("4) VIP");
                    System.out.print("Tipo: ");
                    int tipo = Integer.parseInt(reader.readLine());
                    System.out.print("Nombre: ");
                    String nombre = reader.readLine();
                    System.out.print("Teléfono: ");
                    String tel = reader.readLine();
                    System.out.print("Email: ");
                    String mail = reader.readLine();

                    Cliente nuevo;
                    switch (tipo) {
                        case 1:
                            nuevo = new ClienteRegular(nombre, tel, mail);
                            break;
                        case 2:
                            nuevo = new ClienteFrecuente(nombre, tel, mail);
                            break;
                        case 3:
                            nuevo = new ClienteMayorista(nombre, tel, mail);
                            break;
                        case 4:
                            nuevo = new ClienteVip(nombre, tel, mail);
                            break;
                        default:
                            System.out.println("Tipo inválido, se crea Cliente genérico.");
                            nuevo = new Cliente(nombre, "Desconocido", tel, mail);
                    }
                    tienda.registrarCliente(nuevo);
                    break;

                case 2:
                    List<Cliente> clientes = tienda.getClientes();
                    System.out.println("-- Clientes registrados --");
                    for (int i = 0; i < clientes.size(); i++) {
                        System.out.println((i + 1) + ") " + clientes.get(i).getNombre());
                    }
                    System.out.print("Seleccione su cliente por número: ");
                    int idx = Integer.parseInt(reader.readLine()) - 1;
                    if (idx < 0 || idx >= clientes.size()) {
                        System.out.println("Cliente inválido.");
                        break;
                    }
                    Cliente cliente = clientes.get(idx);

                    Pedido pedido = new Pedido();
                    while (true) {
                        System.out.println("-- Catálogo de productos --");
                        for (Producto p : catalogo) System.out.println(p);
                        System.out.print("ID producto (0 para terminar): ");
                        int pid = Integer.parseInt(reader.readLine());
                        if (pid == 0) break;
                        System.out.print("Cantidad: ");
                        int cant = Integer.parseInt(reader.readLine());
                        Producto seleccionado = null;
                        for (Producto p : catalogo) if (p.getId() == pid) seleccionado = p;
                        if (seleccionado == null) System.out.println("Producto no encontrado.");
                        else pedido.agregarProducto(seleccionado, cant);
                    }
                    float total = cliente.calcularTotalPedido(pedido);
                    tienda.realizarPedido(pedido);
                    System.out.println("Total a pagar (con descuentos/cargos): $" + total);
                    break;

                case 3:
                    tienda.generarResumenDeVentas();
                    break;

                case 4:
                    tienda.consultarHistorial();
                    break;

                case 0:
                    System.out.println("Gracias por su visita.");
                    System.exit(0);

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
