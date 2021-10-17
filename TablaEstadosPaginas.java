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

 import java.util.ArrayList;

 /**
 * Clase que representa la talba de estados de paginas.
 */
public class TablaEstadosPaginas {
 
    /* ****************************************************************
	 * 			Constantes
	 *****************************************************************/

    /**
     * Columna de bit de referencia
     */
    public final static int COL_BIT_REFERENCIA = 0;

    /**
     * Columna de bit de modificación
     */
    public final static int COL_BIT_MODIFICACION = 1;

    /**
     * Columna de categoria de la página (S.O)
     */
    public final static int COL_BIT_CATEGORIA = 2;

    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    
    /**
     * Tabla de estados de página
     */
    private int tablaEstados[][];

    /* ****************************************************************
	 * 			Constructor
	 *****************************************************************/

    /**
     * Contructor de la tabla de estados de Pagina dado un tamaño
     * Inicializa los bits R 8de referencia) y M (de modificación) en 0. 
     * @param tamanio Tamaño de la tabla de estados de página
     */
    public TablaEstadosPaginas( int tamanio){
        tablaEstados = new int[tamanio][3];
        inicializarBits_R_M_C();
    }

    /* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

    /**
     * Se encarga de inicializar los bits R y M de cada página en 0 con sus respectivas clases.
     */
    public synchronized void inicializarBits_R_M_C(){
        for(int i=0; i<tablaEstados.length; i++){
            for(int j=0; j<tablaEstados[i].length; j++){
                tablaEstados[i][j]=0;
            }
        }
    }

    /**
     * Se encarga de actualizar el bit estado para una página dada.
     * @param pagina Página a la cual se le va a actualizar el estado del bit.
     * @param bit Bit R (de referencia) o bit M (de Modificación).
     */
    public synchronized void update_bit(int pagina, String bit){
        if(bit.equals("r")) tablaEstados[pagina][COL_BIT_REFERENCIA] = 1;
        else tablaEstados[pagina][COL_BIT_MODIFICACION] = 1;        
        updateCategory(pagina);
    } 

    /**
     * Se encarga de actualizar la categoría del estado de la página. 
     * @param pagina Página a la cual se le va a actualizar la categoria.
     */
    public synchronized void updateCategory(int pagina){
        int bit_R = tablaEstados[pagina][COL_BIT_REFERENCIA];
        int bit_M = tablaEstados[pagina][COL_BIT_MODIFICACION];
        
        // Categoría 0: No referenciada, No modificada 
        if(bit_R==0 && bit_M==0) tablaEstados[pagina][COL_BIT_CATEGORIA] = 0;

        // Categoría 1: No referenciada, modificada 
        else if(bit_R==0 && bit_M==1) tablaEstados[pagina][COL_BIT_CATEGORIA] = 1;

        // Categoría 2: referenciada, No modificada 
        else if(bit_R==1 && bit_M==0) tablaEstados[pagina][COL_BIT_CATEGORIA] = 2;

        // Categoría 3: referenciada, modificada 
        else tablaEstados[pagina][COL_BIT_CATEGORIA] =  3;        
    }

    /**
     * Se encarga de obtener la categoría dada una referencia de página.
     * @param pagina Página la cual se le quiere obtener la categoría. 
     * @return La categoría de la referencia de página dada. 
     */
    public synchronized int getCategoryPage(int pagina){
        return tablaEstados[pagina][COL_BIT_CATEGORIA];
    }

    /**
     * La interrupción de reloj se encarga de colocar en 0 el bit R de la referencia de págima
     */
    public synchronized void restoreBits_R(){
        for(int i=0; i<tablaEstados.length; i++){
            tablaEstados[i][COL_BIT_REFERENCIA]=0;
            updateCategory(i);
        }
    }

    /**
     * La interrupciónde reloj se encarga de colocar en 0 el bit M de las referencias de páginas que se encuentran fuera de memoria.
     * @param pagesInMemory
     */
    public synchronized void restoreBits_M(ArrayList<Integer> pagesInMemory ){
        for(int i=0; i<tablaEstados.length; i++){
            if(!pagesInMemory.contains(i)){
                tablaEstados[i][COL_BIT_MODIFICACION]=0;
                updateCategory(i);
            }
        }      
    }


}
