import java.util.ArrayList;

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
 * Clase que representa el threadAlgoritmo que se encargará de:  
 * ejecutar el algoritmo de actualización de los bits R y M con base en el algoritmo de reemplazo.
 */
public class ThreadAlgoritmo extends Thread {
    
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
     * SReferencia del thread de actualización.
     */
    private ThreadActualizar threadActualizar;

    /* ****************************************************************
	 * 			Constructor
	 *****************************************************************/

    /**
     * Constructor del threadActualizar que se encarga de actualizar la tabla de páginas y los marcos de página.
     * @param tablaEstadosPaginas Rerencia a la tabla de estados de páginas.
     */
    public ThreadAlgoritmo(TablaEstadosPaginas tablaEstadosPaginas, TablaMarcosPagina tablaMarcosPagina, ThreadActualizar threadActualizar){
        this.tablaEstadosPaginas = tablaEstadosPaginas;
        this.tablaMarcosPagina = tablaMarcosPagina;
        this.threadActualizar = threadActualizar;
    }

    /* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

    @Override
    public void run(){
        while(threadActualizar.getNumReferenciasRestantes()>0){
            // Tiempo de interrupción cada 20 milisegundo después
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            e.printStackTrace();
            }

            // Restablece todos los bits de referencia de la referencias de páginas en 0.
            tablaEstadosPaginas.restoreBits_R();

            // Lee las referencias de página que siguen aún en memoria
            ArrayList<Integer> pagesInMemory =  tablaMarcosPagina.getPages();

            // Posteriormente, se realiza el cambio de bit de modificación a 0 en las referencias de páginas que se encuentren fuera de la memoria.
            tablaEstadosPaginas.restoreBits_M(pagesInMemory);
        }
    }
}
