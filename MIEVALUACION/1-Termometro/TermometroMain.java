import java.io.*;

    class temperaturaInvalidException extends Exception{
        public temperaturaInvalidException(String message){
            super(message);
        }
    }

    class Temperatura{
        private float valor;

        public Temperatura(float valor) throws temperaturaInvalidException{
            setValor(valor);
        }


        public float getValor(){  return valor; }

        public void setValor(float valor) throws temperaturaInvalidException {
          if(valor < -273.15 || valor > 1000){
        throw new temperaturaInvalidException("TEMPERATURA INVALIDA");
        }
        this.valor = valor;  
}


        public String toString(){
            return "Temperatura: "+valor;
        }
    }

    interface Medible{
    public Temperatura medir()throws Exception;    
    }
    
    class Termometro{
        protected String escala;
        protected Temperatura temperaturaActual;
        protected Temperatura tempMaxima;
        protected Temperatura tempMinima;
        protected String tipo;


        public Termometro(String escala, String tipo, float min, float max) throws temperaturaInvalidException {
            this.escala = esEscalaValida(escala) ? escala : "Celsius";
            setTemperaturaMin(new Temperatura(min));
            setTemperaturaMax(new Temperatura(max));
            this.tipo = tipo;
            this.temperaturaActual = new Temperatura((min + max) / 2);
        }

        public String getEscala() {
            return escala;
        }
        
        private boolean esEscalaValida(String escala) {
        return escala != null && (escala.equalsIgnoreCase("Celsius")
                || escala.equalsIgnoreCase("Fahrenheit")
                || escala.equalsIgnoreCase("Kelvin"));
        }

        public void setEscala(String escala) throws temperaturaInvalidException{
            if(!esEscalaValida(escala)){
                throw new temperaturaInvalidException("ESCALA INVALIDA");
            }
        }

        public Temperatura getTemperaturaActual(){
            return temperaturaActual;

        }

        public void setTemperaturaActual(Temperatura temp){
              this.temperaturaActual = temp;
        }

        public Temperatura getTempMin(){
            return tempMinima;
        }
        
        public void setTemperaturaMin(Temperatura temp){
            this.tempMinima = temp;
        }

        public Temperatura getTempMax(){
            return tempMaxima;
        }

        public void setTemperaturaMax(Temperatura temp){
            this.tempMaxima = temp;
        }

        public String getTipo(){
            return tipo;
        }

        public void calibrar(String nuevaEscala){
            if(esEscalaValida(nuevaEscala)){
                this.escala = nuevaEscala;
            }else{
                System.out.println("ESCALA INVALIDAD, EL TERMOMETRO NO SE CALIBRA");
            }
        }

        public void cambiarMedicion(String nuevaEscala){
            if(esEscalaValida(nuevaEscala)){
                this.escala = nuevaEscala;
            }else{
                System.out.println("ESCALA INVALIDA, NO SE CAMBIA LA MEDICION");
            }
        }

        public String toString(){
            return "Termometro: "+tipo+" Escala: "+escala+" Temperatura Actual: "+temperaturaActual+" Temperatura Minima: "+tempMinima+" Temperatura Maxima: "+tempMaxima;
        }


    }

    class TermometroDigital extends Termometro implements Medible{

       private static final String RUTA_ARCHIVO = "C:/Users/ROBERTO/Desktop/MIEVALUACION/1-Termometro/ultima_medicion.txt";


        public TermometroDigital(String escala, float min, float max) throws temperaturaInvalidException, IOException{
            super(escala, "Digital", min, max);
            cargarUltimaTemperatura();
        }

        private void cargarUltimaTemperatura() throws IOException, temperaturaInvalidException {
        File archivo = new File(RUTA_ARCHIVO);

        if (archivo.exists()) {
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                String linea = lector.readLine();
                try {
                    float valor = Float.parseFloat(linea);
                    setTemperaturaActual(new Temperatura(valor));
                    System.out.println("Cargando última temperatura: " + getTemperaturaActual() + "° " + escala);
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: formato inválido en el archivo de temperatura.");
                }
            }
        }
    }
    
    private void guardarUltimaTemperatura() throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            escritor.write(String.valueOf(getTemperaturaActual().getValor()));
        }

    }


    public Temperatura medir() throws temperaturaInvalidException, IOException {
        float valor = (float) (Math.random() * (tempMaxima.getValor() - tempMinima.getValor()) + tempMinima.getValor());
        Temperatura t = new Temperatura(valor);
        setTemperaturaActual(t);
        System.out.println("[Digital] Medida: " + t + "°" + escala);
        guardarUltimaTemperatura();
        return t;
    }

    }

    class TermometroAnalogico extends Termometro implements Medible{
             
                public TermometroAnalogico(String escala,float min, float max) throws temperaturaInvalidException{
                    super(escala, "Analogico", min,max);
                }

    public Temperatura medir() throws temperaturaInvalidException{
        float valor = (float) (Math.random() * (tempMaxima.getValor() - tempMinima.getValor()) + tempMinima.getValor());
        Temperatura t = new Temperatura(valor);
        setTemperaturaActual(t);
        System.out.println("[Analogico] Medida: " + t + "° " + escala);
        return t;
    }
    
    }

    public class TermometroMain{
             
        public static void main(String[] args){

            try(BufferedReader br = new BufferedReader(new InputStreamReader (System.in))){
                Termometro seleccionado;
                  
                while(true){
                     System.out.println("Seleccione tipo de termómetro:\n1) Digital\n2) Analógico");
                     String entrada = br.readLine();
                    
                     try{
                       int tipo = Integer.parseInt(entrada);
                          if(tipo == 1){
                            seleccionado = new TermometroDigital("Celsius",  -50, 150);
                            break;
                          }else if(tipo == 2){
                              seleccionado = new TermometroAnalogico("Celsius", -50, 150);
                              break;
                          }else{
                            System.out.println("Opcion no valida, intente de nuevo");
                          }
                     }catch(NumberFormatException e){
                        System.out.println("Debes ingresar 1 o 2 intente de nuevo");
                     }
                }

                boolean salir = false;
                while(!salir){
                    System.out.println("\n--- Menú Principal ---");
                    System.out.println("1) Medir temperatura");
                    System.out.println("2) Cambiar escala de visualización");
                    System.out.println("3) Calibrar termómetro");
                    System.out.println("4) Mostrar estado del termómetro");
                    System.out.println("5) Salir");
                    System.out.print("Seleccione una opción: ");
                    String opcionStr = br.readLine();

                    int opcion;

                    try{
                        opcion = Integer.parseInt(opcionStr);
                    }catch(NumberFormatException e){
                        System.out.println("Opción no válida, intente de nuevo.");
                        continue;
                    }
                    switch (opcion){
                        case 1: 
                          try {
                            ((Medible) seleccionado).medir();
                          }catch(Exception e){
                            System.out.println("Error al medir temperatura: " + e.getMessage());
                          }
                          break;

                        case 2:
                           System.out.print("Nueva escala (Celsius/Fahrenheit/Kelvin): ");
                           String escala = br.readLine();
                           seleccionado.cambiarMedicion(escala);
                           break;

                        case 3:
                            System.out.print("Nueva escala de calibración (Celsius/Fahrenheit/Kelvin): ");
                            String nuevaEscala = br.readLine();
                            seleccionado.calibrar(nuevaEscala);
                            break;

                        case 4:
                        System.out.println(seleccionado);
                        break;

                        case 5:
                        salir = true;
                        break;
                        default:
                        System.out.println("Opción no válida. Ingresa un número del 1 al 5.");

                        }

                }

                    
            }catch( IOException | temperaturaInvalidException e ){
                System.out.println("Error de entrada/salida: " + e.getMessage());
            }

        }

    }
