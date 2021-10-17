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
 * Clase que representa la tabla de marcos de página en memoria real
 */
public class TablaMarcosPagina {

    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    
    /**
     * Tabla de marcos de página en memoria real.
     */
    private ArrayList<Integer> tablaMarcos;

    /**
      * Representa el tamaño marcos de página
     */
    private int tamanio; 

    /* ****************************************************************
	 * 			Constructor
	 *****************************************************************/

    /**
     * Constructor de la tabla de marcos de página en memoria real
     * @param tamanio Tamaño de la tabla de marcos de página
     */
    public TablaMarcosPagina( int tamanio){
        this.tamanio = tamanio; 
        this.tablaMarcos = new ArrayList<Integer>();
    }

    /* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

    /**
     * Se encarga de añadir una referencia de página a la tabla de marcos de página.
     * @param page Página a actualizar en la tabla de marcos de página.
     * @return True si se logra actualizar. De lo contrario, false. 
     */
    public synchronized boolean addReference(int page){
        if(tablaMarcos.size()<tamanio){
            tablaMarcos.add(page);
            return true; 
        }
        return false; 
    }

    /**
     * Se encarga de verificar si la referencia de página ya se encuentra en la tabla de memoria. 
     * @param page Página a verificar si se encuentra en memoria.
     * @return True si contiene la referencia de página. De lo contrario, false. 
     */
    public synchronized boolean containReference(int page){
        return tablaMarcos.contains(page);
    }

    /**
     * Se encarga de obtener una referencia de página de la tabla marcos de página.
     * @param i Posición de la referencia de página a buscar.
     * @return Entero que representa la referencia de página en esa posición.
     */
    public synchronized Integer getReference(int i){    
        return tablaMarcos.get(i);
    }

    /**
     * Se encarga de remplazar una referencia de página por una nueva dada una posición y la referencia de página nueva. 
     * @param pos Posición de la referencia de página a retirar.
     * @param page Página nueva a remplazar.
     */
    public synchronized void replaceReference(int pos, int page){
        tablaMarcos.set(pos, page);
    }

    public synchronized ArrayList<Integer> getPages(){
        return tablaMarcos;
    }

    /**
     * Indica el tamaño de la tabla de marcos de memoria.
     * @return Tamaño de la tabla de marcos de memoria. 
     */
    public synchronized int size(){
        return tablaMarcos.size();
    }

}
