import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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

/**
 * Clase que representa el threadActualizar que se encargará de: Actualizar el
 * estado de la tabla de páginas. Actualizar los marcos de página en memoria
 * real de acuerdo con las referencias generadas por el proceso.
 */
public class ThreadActualizar extends Thread {

    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/

    /**
     * Referencia a la tabla de estados de páginas.
     */
    private TablaEstadosPaginas tablaEstadosPaginas;

    /**
     * Referencia a la tabla de marcos de página de memoria real.
     */
    private TablaMarcosPagina tablaMarcosPagina;

    /**
     * Secuencia de referencias (a paginas del proceso).
     */
    private String secReferencias;

    /**
     * Número de fallo de páginas
     */
    private int pagesFault;

    /**
     * Número de secuencias de referencias de páginas restantes
     */
    private int numSecReferenciasRestantes;

    /**
     * Barrera del fin de la ejecución.
     */
    private CyclicBarrier exit;   

    /* ****************************************************************
	 * 			Contructor
	 *****************************************************************/

    /**
     * Constructor del threadActualizar que se encarga de actualizar la tabla de
     * páginas y los marcos de página.
     * 
     * @param tablaEstadosPaginas Rerencia a la tabla de estados de páginas.
     * @param tablaMarcosPagina   Referencia a la tabla de marcos de páginas.
     * @param numReferencias      Número de referencias en el archivo.
     * @param secReferencias      Secuencia de referencias (a páginas del proceso).
     */
    public ThreadActualizar(TablaEstadosPaginas tablaEstadosPaginas, TablaMarcosPagina tablaMarcosPagina, int numReferencias, String secReferencias, CyclicBarrier exit) {
        this.numSecReferenciasRestantes = numReferencias; 
        this.tablaEstadosPaginas = tablaEstadosPaginas;
        this.tablaMarcosPagina = tablaMarcosPagina;
        this.secReferencias = secReferencias;
        this.pagesFault = 0;
        this.exit = exit; 
        
    }

    /* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    
    /**
     * Se encarga de dar el npumero de fallo de páginas. 
     */
    public int darFalloPaginas(){
        return pagesFault;
    }

     /**
     * Se encarga de retornar el número de referencias restantes
     */
    public int getNumReferenciasRestantes(){
        return numSecReferenciasRestantes;
    }

    @Override
    public void run() {
        String[] secs = secReferencias.split(" ");
        for (String s : secs) {
            // Carga de la nueva referencia 1 milisegundo después
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int page = Integer.parseInt(s.split(",")[0]);
            String bit = s.split(",")[1];

            // Actualiza el estado de la tabla de páginas.
            tablaEstadosPaginas.update_bit(page, bit);

            // Verifica si la referencias de página ya se encuentar en la tabla marcos de página.
            if (!tablaMarcosPagina.containReference(page)) {
                // Aumenta el número de fallas de paginas
                pagesFault++;

                // Intenta añadir a la tabla de marcos de memoria RAM la referencia de página
                if (!tablaMarcosPagina.addReference(page)) {

                    int posToChange=0;
                    boolean cent = false;
                    int categoria = 0;

                    // Mientras la variable centinela sea false, se buscará la pagina que cumpla con la precedencia de clases
                    while (!cent) {
                        for (int i = 0; i < tablaMarcosPagina.size() && !cent; i++) {
                            Integer pageFound = tablaMarcosPagina.getReference(i);

                            // Compara la categoria de cada ref. de pagina de acuerdo con el algoritmo de envegecimiento.
                            if (categoria == tablaEstadosPaginas.getCategoryPage(pageFound)) {

                                // Se almacena la posición de la página que se va a retirar.
                                posToChange =i;
                                cent = true;
                            }
                        }

                        // Si no se ha encontrado paginas con la categoria 0, entonces empieza la busqueda por la categoria 1
                        if (!cent) categoria++;
                    }
                    tablaMarcosPagina.replaceReference(posToChange, page);
                }
            }
            numSecReferenciasRestantes--;
        }

        try {
            exit.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}