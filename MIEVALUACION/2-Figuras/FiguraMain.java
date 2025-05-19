import java.io.*;
 
class FiguraException extends Exception{
    public FiguraException(String mensaje){
        super(mensaje);
    }
}

interface  Dibujable {
    public void show(); 
}

abstract class Figuras {
     private String nombre;

     public Figuras(String nombre){
        setNombre(nombre);
     }

    public String getNombre(){  return nombre;  }

    public void setNombre(String nombre){
        this.nombre = (nombre == null || nombre.isEmpty()) ? "Figura" : nombre;
    }

    public abstract double area() throws FiguraException;
    public abstract double perimetro()  throws FiguraException;

    public String toString(){
        return "Nombre: " + getNombre();
    }
}

class Cuadrado extends Figuras implements Dibujable {

    private double lado;
    public Cuadrado(String nombre, double lado)throws FiguraException{
        super("Cuadrado");
        setLado(lado);
    }

    public double getLado(){  return lado;  }

    public void setLado(double lado) throws FiguraException{
        if (lado <= 0) {
            this.lado = 1;
            throw new FiguraException("El lado debe ser mayor que cero");
        }
        this.lado = lado;
    }
   
     public double area(){
        return lado * lado;
     }

     public double perimetro(){
        return lado * 4;
     }
     
    @Override
    public String toString() {
        return super.toString() + " - Lado: " + lado +
               ", Área: " + area() +
               ", Perímetro: " + perimetro();
    }
     @Override
    public void show() {
        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

}

class Rectangulo extends Figuras implements Dibujable {

    private double base, altura;

     public Rectangulo(String nombre, double base, double altura) throws FiguraException{
        super("Rectangulo");
        setBase(base);
        setAltura(altura);
    }

    public double getBase(){ return base;}

    public void setBase(double base) throws FiguraException{
        if (base <= 0) {
            this.base = 1;
            throw new FiguraException("La base debe ser mayor que cero");
        }
        this.base = base;
    }

    public double getAltura(){ return altura;}

    public void setAltura(double altura) throws FiguraException{
        if (altura <= 0) {
            this.altura = 1;
            throw new FiguraException("La altura debe ser mayor que cero");
        }
        this.altura = altura;
    }
   
    public double area(){
        return base * altura;
    }

    public double perimetro(){
        return (base * 2) + (altura * 2);
    }

    @Override
    public String toString() {
        return super.toString() + " - Base: " + base +
               ", Altura: " + altura +
               ", Área: " + area() +
               ", Perímetro: " + perimetro();
    }

    @Override
    public void show() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < base; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

}

class Circulo extends Figuras implements Dibujable{

    private double radio;

    public Circulo(String nombre, double radio) throws FiguraException{
        super("Circulo");
        setRadio(radio);
    }

    public double getRadio(){ return radio; }
    
    public void setRadio(double radio) throws FiguraException{
        if (radio <= 0) {
            this.radio = 1;
            throw new FiguraException("El radio debe ser mayor que cero");
        }
        this.radio = radio;
    }

    public double area(){
        return Math.PI * radio * radio;
    }

    public double perimetro(){
        return 2 * Math.PI * radio;
    }

    @Override
    public String toString() {
        return super.toString() + " - Radio: " + radio +
               ", Área: " + area() +
               ", Perímetro: " + perimetro();
    }

    @Override
    public void show() {
        System.out.println("   ***   ");
        System.out.println(" *     * ");
        System.out.println("*       *");
        System.out.println(" *     * ");
        System.out.println("   ***   ");
    }
}

enum TipoTriangulo {
    EQUILATERO,
    ISOSCELES,
    ESCALENO
}

class Triangulo extends Figuras implements Dibujable {

    private double lado1, lado2, lado3;
    private TipoTriangulo tipo;

    public Triangulo(double lado1, double lado2, double lado3) throws FiguraException {
        super("Triángulo");
        setLados(lado1, lado2, lado3);
        determinarTipo();
    }

    private void setLados(double l1, double l2, double l3) throws FiguraException {
        if (l1 <= 0 || l2 <= 0 || l3 <= 0)
            throw new FiguraException("Los lados deben ser mayores que cero");
        if (!esTrianguloValido(l1, l2, l3))
            throw new FiguraException("Los lados no forman un triángulo válido");

        this.lado1 = l1;
        this.lado2 = l2;
        this.lado3 = l3;
    }

    private boolean esTrianguloValido(double a, double b, double c) {
        return a + b > c && a + c > b && b + c > a;
    }

    private void determinarTipo() {
        if (lado1 == lado2 && lado2 == lado3) {
            tipo = TipoTriangulo.EQUILATERO;
        } else if (lado1 == lado2 || lado1 == lado3 || lado2 == lado3) {
            tipo = TipoTriangulo.ISOSCELES;
        } else {
            tipo = TipoTriangulo.ESCALENO;
        }
    }

    public TipoTriangulo getTipo() {
        return tipo;
    }

    @Override
    public double area() {
        // Fórmula de Herón
        double s = (lado1 + lado2 + lado3) / 2;
        return Math.sqrt(s * (s - lado1) * (s - lado2) * (s - lado3));
    }

    @Override
    public double perimetro() {
        return lado1 + lado2 + lado3;
    }

    @Override
    public void show() {
        System.out.println("   *   ");
        System.out.println("  * *  ");
        System.out.println(" *   * ");
        System.out.println("*******");
    }

    @Override
    public String toString() {
        return super.toString() +
               " - Lados: " + lado1 + ", " + lado2 + ", " + lado3 +
               "\nTipo: " + tipo +
               "\nÁrea: " + area() +
               "\nPerímetro: " + perimetro();
    }
}

class Trapecio extends Figuras implements Dibujable{ 

    private double baseMayor, baseMenor, altura;

    public Trapecio(String nombre, double baseMayor, double baseMenor, double altura) throws FiguraException{
        super("Trapecio");
        setBaseMayor(baseMayor);
        setBaseMenor(baseMenor);
        setAltura(altura);
    }

    public double getBaseMayor(){ return baseMayor; }

    public void setBaseMayor(double baseMayor) throws FiguraException{
        if (baseMayor <= 0) {
            this.baseMayor = 1;
            throw new FiguraException("La base mayor debe ser mayor que cero");
        }
        this.baseMayor = baseMayor;
    }

    public double getBaseMenor(){ return baseMenor; }

    public void setBaseMenor(double baseMenor) throws FiguraException{
        if (baseMenor <= 0) {
            this.baseMenor = 1;
            throw new FiguraException("La base menor debe ser mayor que cero");
        }
        this.baseMenor = baseMenor;
    }

    public double getAltura(){ return altura; }

    public void setAltura(double altura) throws FiguraException{
        if (altura <= 0) {
            this.altura = 1;
            throw new FiguraException("La altura debe ser mayor que cero");
        }
        this.altura = altura;
    }

    public double area(){
        return ((baseMayor + baseMenor) * altura) / 2;
    }

    public double perimetro(){
        return baseMayor + baseMenor + (2 * altura);
    }

    public String toString() {
        return super.toString() + " - Base Mayor: " + baseMayor +
               ", Base Menor: " + baseMenor +
               ", Altura: " + altura +
               ", Área: " + area() +
               ", Perímetro: " + perimetro();
    }
    @Override
    public void show() {
        System.out.println("   *****   ");
        System.out.println("  *     *  ");
        System.out.println(" *       * ");
        System.out.println("*         *");
        System.out.println("***********");
    }
}


public class FiguraMain {
    public static void main(String[] args) {
        try {
            menu();
        } catch (Exception e) {
            System.out.println("Error inesperado en el programa: " + e.getMessage());
        }
    }

    public static void menu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Figuras figura = null;
        int opcionFigura = 0, opcionAccion = 0;

        try {
            do {
                System.out.println("\nSeleccione una figura:");
                System.out.println("1. Cuadrado");
                System.out.println("2. Rectángulo");
                System.out.println("3. Trapecio");
                System.out.println("4. Círculo");
                System.out.println("5. Triángulo");
                System.out.println("6. Salir");
                System.out.print("Opción: ");
                opcionFigura = leerEntero(reader);

                if (opcionFigura == 6) break;

                try {
                    switch (opcionFigura) {
                        case 1:
                            double lado = leerDouble(reader, "Ingrese el lado del cuadrado: ");
                            figura = new Cuadrado("Cuadrado", lado);
                            break;
                        case 2:
                            double base = leerDouble(reader, "Ingrese la base del rectángulo: ");
                            double altura = leerDouble(reader, "Ingrese la altura del rectángulo: ");
                            figura = new Rectangulo("Rectángulo", base, altura);
                            break;
                        case 3:
                            double baseMayor = leerDouble(reader, "Ingrese la base mayor del trapecio: ");
                            double baseMenor = leerDouble(reader, "Ingrese la base menor del trapecio: ");
                            double alturaTrap = leerDouble(reader, "Ingrese la altura del trapecio: ");
                            figura = new Trapecio("Trapecio", baseMayor, baseMenor, alturaTrap);
                            break;
                        case 4:
                            double radio = leerDouble(reader, "Ingrese el radio del círculo: ");
                            figura = new Circulo("Círculo", radio);
                            break;
                        case 5:
                            figura = crearTriangulo(reader);
                            break;
                        default:
                            System.out.println("Opción inválida, intente de nuevo.");
                            continue;
                    }
                } catch (FiguraException e) {
                    System.out.println("Error al crear la figura: " + e.getMessage());
                    continue;
                }

                // Menú de acciones sobre la figura creada
                do {
                    try {
                        System.out.println("\n¿Qué desea hacer con la figura?");
                        System.out.println("1. Calcular área");
                        System.out.println("2. Calcular perímetro");
                        System.out.println("3. Mostrar detalles");
                        System.out.println("4. Mostrar forma de la figura");
                        System.out.println("5. Volver al menú principal");
                        System.out.print("Opción: ");
                        opcionAccion = leerEntero(reader);

                        switch (opcionAccion) {
                            case 1:
                                System.out.println("Área: " + figura.area());
                                break;
                            case 2:
                                System.out.println("Perímetro: " + figura.perimetro());
                                break;
                            case 3:
                                System.out.println(figura);
                                break;
                            case 4:
                                if (figura instanceof Dibujable) {
                                    ((Dibujable) figura).show();
                                } else {
                                    System.out.println("Esta figura no se puede dibujar.");
                                }
                                break;
                            case 5:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción inválida, intente de nuevo.");
                        }
                    } catch (FiguraException e) {
                        System.out.println("Error al realizar acción: " + e.getMessage());
                    }
                } while (opcionAccion != 5);

            } while (true);
        } catch (Exception e) {
            System.out.println("Error en el menú: " + e.getMessage());
        } finally {
            try {
                reader.close();
                System.out.println("Programa finalizado.");
            } catch (IOException e) {
                System.out.println("Error al cerrar el lector: " + e.getMessage());
            }
        }
    }

    private static int leerEntero(BufferedReader reader) throws IOException {
        while (true) {
            try {
                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor, ingrese un número entero: ");
            }
        }
    }

    private static double leerDouble(BufferedReader reader, String mensaje) throws IOException {
        double valor;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(reader.readLine());
                if (valor <= 0) {
                    System.out.println("El valor debe ser mayor que cero. Intente de nuevo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
            }
        }
    }

    private static Triangulo crearTriangulo(BufferedReader reader) throws IOException {
        try {
            double l1 = leerDouble(reader, "Ingrese el lado 1 del triángulo: ");
            double l2 = leerDouble(reader, "Ingrese el lado 2 del triángulo: ");
            double l3 = leerDouble(reader, "Ingrese el lado 3 del triángulo: ");
            return new Triangulo(l1, l2, l3);
        } catch (FiguraException e) {
            System.out.println("Error al crear triángulo: " + e.getMessage());
            return crearTriangulo(reader);
        }
    }
}
