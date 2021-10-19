/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2203 -  Infrastructura Computacional
 * Proyecto: Memoria Virtual
 * @version 1.0
 * @author 
 * Octubre de 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

 /**
 * Clase que representa el Main. 
 */
public class Main{

    /* ****************************************************************
	 * 			Constantes
	 *****************************************************************/

    /**
     * Ruta de archivo de configuración de tamaño de página 16
     */
    private final static String CONFIG_TAM_16 = "./config/referencias8_16_75.txt";

     /**
     * Ruta de archivo de configuración de tamaño de página 32
     */
    private final static String CONFIG_TAM_32 = "./config/referencias8_32_75.txt";

    /**
     * Ruta de archivo de configuración de tamaño de página 64
     */
    private final static String CONFIG_TAM_64 = "./config/referencias8_64_75.txt";

    /**
     * Ruta de archivo de configuración de tamaño de página 128
     */
    private final static String CONFIG_TAM_128 = "./config/referencias8_128_75.txt";

    /**
     * Ruta de archivo de configuración de tamaño de página 32
     */
    private final static String CONFIG_TAM_ESCENARIO1 = "./config/TestReferencias16_32.txt";


    /**
     * Ruta de archivo de configuración de tamaño de página 64
     */
    private final static String CONFIG_TAM_ESCENARIO2 = "./config/TestReferencias32_64.txt";



    /**
     * Indica el reporte final del programa 
     * @return Reporte del programa.
     */
    public static String toString(String numMarcos, String numPaginas, String sec, int numFallos){
        String msg = "\n\n-------------------- REPORTE --------------------" +
        "\n\nNúmero de marcos de página: " + numMarcos +
        "\n\nNúmero de páginas: " + numPaginas +
        "\n\nSecuencia de referencias: " + sec +
        "\n\nNúmero de fallo de páginas: " + numFallos;
        return msg;
    }

    /* ****************************************************************
	 * 			Main
	 *****************************************************************/

    /**
     * Main de la aplicación.
     * Se encarga de leer el fichero .txt configurable y crear las instancias necesarioas para la ejecución del programa.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String secReferencias = "";
        FileReader f = new FileReader(CONFIG_TAM_ESCENARIO2);
        BufferedReader b = new BufferedReader(f);

        // Número de marcos de página en memoria RAM que el sistema le asigna al proceso 
        String numMarcosPagina = b.readLine(); 
        // Número de páginas del proceso
        String numPaginas = b.readLine();
        // Número de referencias en el archivo
        String numReferenciasArchivos = b.readLine();
        
        for(int i=0; i<Integer.parseInt(numReferenciasArchivos); i++){
            secReferencias = secReferencias + b.readLine()+ " ";
        }
        b.close();

        // Creación de tabla de estados de página
        TablaEstadosPaginas tablaEstadosPaginas = new TablaEstadosPaginas(Integer.parseInt(numPaginas));
        
        // Creación de tabla de marcos de página 
        TablaMarcosPagina tablaMarcosPagina = new TablaMarcosPagina(Integer.parseInt(numMarcosPagina));
    
        // Indica el fin de la ejecución del programa.
        CyclicBarrier exit = new CyclicBarrier(2);

        // Creación de Thread de actualización 
        ThreadActualizar threadActualizar = new ThreadActualizar(tablaEstadosPaginas, tablaMarcosPagina, Integer.parseInt(numReferenciasArchivos), secReferencias, exit);
        threadActualizar.start();

        // Creación de Thread de ejecución de algoritmo de reemplazo  
        ThreadAlgoritmo threadAlgoritmo = new ThreadAlgoritmo(tablaEstadosPaginas, tablaMarcosPagina, threadActualizar);
        threadAlgoritmo.start();

        try {
            exit.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println( toString(numMarcosPagina, numPaginas, secReferencias, threadActualizar.darFalloPaginas()) );
    }
}