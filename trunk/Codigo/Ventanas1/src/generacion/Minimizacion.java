/*
 * Trabajo Práctico de Compiladores 2008.
 * 10mo Semestre Ingeniería Infomática.
 * Facultad Politécnica - UNA.
 */
package generacion;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.List;
import java.util.Arrays;

/**
 * Esta clase implementa el algoritmo de minimización de 
 * estados de un AFD.
 * @author Germán Hüttemann
 * @author Marcelo Rodas
 */
public class Minimizacion {

    /**
     * Obtiene un <code>AFD</code> mínimo a partir de un 
     * <code>AFD</code> determinado.
     * @param afd El <code>AFD</code> a minimizar.
     * @return Un <code>AFD</code> equivalente a <code>afd</code> 
     * pero con la menor cantidad de estados posibles.
     */
    public static AFDMin getAFDminimo(AFD afd) {
        /* Eliminamos los estados inalcanzables */
        AFD afdPostInalcanzables = new AFD();
        copiarAutomata(afd, afdPostInalcanzables);
        eliminarInalcanzables(afdPostInalcanzables);
        
        /* Proceso de minimización */
        AFD afdPostMinimizacion = minimizar(afdPostInalcanzables);
        
        /* Eliminamos estados identidad */
        AFD afdPostIdentidades = new AFD();
        copiarAutomata(afdPostMinimizacion, afdPostIdentidades);
        eliminarIdentidades(afdPostIdentidades);
        
        /* TODO:
         * - Construir el AFDMin.
         * - Comparar si hubo cambios entre AFDs intermedios.
         * - Guardar rastro del proceso.
         */
        return new AFDMin();
    }
    
    /**
     * Elimina los estados inalcanzables de un AFD.
     * @param afd El AFD sobre el cual se eliminan estados inalcanzables.
     */
    private static void eliminarInalcanzables(AFD afd) {
        /* Conjunto de estados alcanzados desde el estado inicial */
        Conjunto<Estado> alcanzados = recuperarAlcanzados(afd);
        
        /* Eliminamos los estados no alcanzados */
        afd.getEstados().retener(alcanzados);
        
        /* 
         * No se actualizan los identificadores de 
         * para que pueda notarse cuales fueron
         * eliminados, si los hay.
         */
    }
    
    /**
     * A partir del estado inicial de un AFD recupera los
     * estados alcanzados, realizando un recorrido DFS
     * no recursivo (utiliza una pila).
     * @param afd El AFD cuyos estados alcanzados deben ser
     * recuperados.
     * @return El conjunto de estados recuperados.
     */
    private static Conjunto<Estado> recuperarAlcanzados(AFD afd) {
        /* Estado inicial del recorrido */
        Estado actual = afd.getEstadoInicial();
        
        /* Conjunto de estados alcanzados */
        Conjunto<Estado> alcanzados = new Conjunto<Estado>();
        
        /* Agregamos el estado actual */
        alcanzados.agregar(actual);
        
        /* Pila para almacenar los estados pendientes */
        Stack<Estado> pila = new Stack<Estado>();
        
        /* Meter el estado actual como el estado inicial */
        pila.push(actual);
        
        while (!pila.isEmpty()) {
            actual = pila.pop();
            
            for (Transicion t : actual.getTransiciones()) {
                Estado e = t.getEstado();
                
                if (!alcanzados.contiene(e)) {
                    alcanzados.agregar(e);
                    pila.push(e);
                }
            }
        }
        
        return alcanzados;
    }
    
    /**
     * Implementación del algoritmo de minimización de
     * estados. Algoritmo 3.39, libro de Compiladores
     * de Aho.
     * @param afdMinimo
     */
    private static AFD minimizar(AFD afd) {
        /* Tablas Hash auxiliares */
        Hashtable<Estado, Conjunto<Integer>> tabla1;
        Hashtable<Conjunto<Integer>, Conjunto<Estado>> tabla2;
        
        /* Conjunto de las particiones del AFD */
        Conjunto<Conjunto<Estado>> particion = new Conjunto<Conjunto<Estado>>();
        
        /* 
         * Paso 1:
         * =======
         * Separar el AFD en dos grupos, los estados finales y
         * los estados no finales.
         */
        particion.agregar(afd.getEstadosNoFinales());
        particion.agregar(afd.getEstadosFinales());
        
        /*
         * Paso 2:
         * =======
         * Construcción de nuevas particiones
         */ 
        Conjunto<Conjunto<Estado>> nuevaParticion;
        
        while (true) {
            /* Conjunto de nuevas particiones en cada pasada */
            nuevaParticion = new Conjunto<Conjunto<Estado>>();
            
            for (Conjunto<Estado> grupo : particion) {
                /* 
                 * Los grupos unitarios son ignorados debido
                 * a que ya no pueden ser particionados.
                 */
                if (grupo.cantidad() <= 1) {
                    nuevaParticion.agregar(grupo);
                }
                else {
                    /*
                     * Paso 2.1:
                     * =========
                     * Hallamos los grupos alcanzados por
                     * cada estado del grupo actual.
                     */
                    tabla1 = new Hashtable<Estado, Conjunto<Integer>>();
                    for (Estado e : grupo)
                        tabla1.put(e, getGruposAlcanzados(e, particion));
                    
                    /*
                     * Paso 2.2:
                     * =========
                     * Calculamos las nuevas particiones
                     */
                    tabla2 = new Hashtable<Conjunto<Integer>, Conjunto<Estado>>();
                    for (Estado e : grupo) {
                        Conjunto<Integer> alcanzados = tabla1.get(e);
                        if (tabla2.containsKey(alcanzados))
                            tabla2.get(alcanzados).agregar(e);
                        else {
                            Conjunto<Estado> tmp = new Conjunto<Estado>();
                            tmp.agregar(e);
                            tabla2.put(alcanzados, tmp);
                        }
                    }
                    
                    /*
                     * Paso 2.3:
                     * =========
                     * Copiar las nuevas particiones al conjunto de
                     * nuevas particiones.
                     */
                    for (Conjunto<Estado> c : tabla2.values())
                        nuevaParticion.agregar(c);
                }
            }
            
            /* Ordenamos la nueva partición */
            nuevaParticion.ordenar();
            
            /* 
             * Paso 2.4:
             * =========
             * Si las particiones son iguales, significa que no
             * hubo cambios y debemos terminar. En caso contrario,
             * seguimos particionando.
             */
            if (nuevaParticion.equals(particion))
                break;
            else
                particion = nuevaParticion;
        }
        
        /* 
         * Paso 3:
         * =======
         * Debemos crear el nuevo AFD, con
         * los nuevos estados producidos.
         */
        AFD afdPostMinimizacion = new AFD(afd.getAlfabeto(), afd.getExprReg());
        
        /* 
         * Paso 3.1:
         * =========
         * Agregamos los estados al nuevo AFD. Para
         * los estados agrupados, colocamos una
         * etiqueta distintiva, para que pueda notarse
         * resultado de cuáles estados es.
         */
        for (int i=0; i < particion.cantidad(); i++) {
            Conjunto<Estado> grupo = particion.obtener(i);
            Boolean esFinal = false;
            
            /* 
             * El grupo actual tiene un estado final,
             * el estado correspondiente en el nuevo
             * AFD también debe ser final.
             */
            if (tieneEstadoFinal(grupo))
                esFinal = true;
            
            /*
             * Si el estado es resultado de la unión de
             * dos o más estados, su etiqueta será del 
             * tipo e1.e2.e3, donde e1, e2 y e3 son los
             * estados agrupados (aparecen separados por
             * un punto).
             */
            String etiqueta = obtenerEtiqueta(grupo);
            
            /*
             * Agregamos efectivamente el estado
             * al nuevo AFD.
             */
            Estado estado = new Estado(i, esFinal);
            estado.setEtiqueta(etiqueta);
            afdPostMinimizacion.agregarEstado(estado);
        }
        
        /*
         * Paso 3.2:
         * =========
         * Generamos un mapeo de grupos (estados del nuevo AFD)
         * a estados del AFD original, de manera a que resulte
         * sencillo obtener los estados adecuados en el momento
         * de agregar las transiciones al nuevo AFD.
         */
        Hashtable<Estado, Estado> mapeo = new Hashtable<Estado, Estado>();
        for (int i=0; i < particion.cantidad(); i++) {
            /* Grupo a procesar */
            Conjunto<Estado> grupo = particion.obtener(i);
            
            /* Estado del nuevo AFD */
            Estado valor = afdPostMinimizacion.getEstado(i);
            
            /* Guardar mapeo */
            for (Estado clave : grupo)
                mapeo.put(clave, valor);
        }
        
        /* 
         * Paso 3.3:
         * =========
         * Agregamos las transiciones al nuevo AFD utilizando
         * el mapeo de estados entre dicho AFD y el AFD original,
         * realizado en el paso 3.2.
         */
        for (int i=0; i < particion.cantidad(); i++) {
            /* Estado representante del grupo actual */
            Estado representante = particion.obtener(i).obtenerPrimero();
            
            /* Estado del nuevo AFD */
            Estado origen = afdPostMinimizacion.getEstado(i);
            
            /* Agregamos las transciones */
            for (Transicion trans : representante.getTransiciones()) {
                Estado destino = mapeo.get(trans.getEstado());
                origen.getTransiciones().agregar(new Transicion(destino, trans.getSimbolo()));
            }
        }
        
        return afdPostMinimizacion;
    }
    
    /**
     * Para un estado dado, busca los grupos en los que 
     * caen las transiciones del mismo.
     * @param estado El estado para el cual buscar los grupos alcanzados.
     * @param particion El conjunto de grupos de estados sobre el cual buscar.
     * @return Un conjunto de enteros que representan las posiciones de los
     * grupos alcanzados dentro del conjunto de grupos.
     */
    private static Conjunto<Integer> getGruposAlcanzados(Estado estado, Conjunto<Conjunto<Estado>> particion) {
        /* Grupos alcanzados por el estado */
        Conjunto<Integer> gruposAlcanzados = new Conjunto<Integer>();
        
        /* Obtener grupo alcanzado por cada transición */
        for (Transicion t : estado.getTransiciones()) {
            Estado destino = t.getEstado();
            
            /* Buscar grupo alcanzado */
            for (Conjunto<Estado> grupo : particion) {
                Integer idGrupo = particion.obtenerPosicion(grupo);
                if (grupo.contiene(destino) && !gruposAlcanzados.contiene(idGrupo)) {
                    gruposAlcanzados.agregar(idGrupo);
                    
                    /*
                     * Debido a que un estado dado siempre
                     * estará en un solo grupo, paramos de
                     * buscar ya que no habrá otro grupo
                     * alcanzado por el estado en cuestión.
                     */
                    break;
                }
            }
        }
        
        return gruposAlcanzados;
    }
    
    /**
     * Elimina los estados identidad, aquellos que para todos
     * los símbolos del alfabeto tienen transiciones a sí mismos.
     * Este tipo de estados sólo deben ser eliminados si no son
     * estados de aceptación.
     * @param afd El AFD sobre el cual eliminar estados identidad.
     */
    private static void eliminarIdentidades(AFD afd) {
        /* Conjunto de estados a eliminar */
        Conjunto<Estado> estadosEliminados = new Conjunto<Estado>();
        
        /* Seleccionamos los estados identidad no finales */
        for (Estado e : afd.getEstados())
            if (e.getEsIdentidad() && !e.getEsFinal())
                estadosEliminados.agregar(e);
        
        if (estadosEliminados.estaVacio())
            return;
        
        /* Eliminamos los estados identidad no finales */
        for (Estado e : estadosEliminados)
            afd.getEstados().eliminar(e);
        
        /* Transiciones a eliminar */
        Vector<List> transEliminadas = new Vector<List>();
        
        /* Seleccionamos las transiciones colgadas */
        for (Estado e : afd.getEstados())
        for (Transicion t : e.getTransiciones())
            if (estadosEliminados.contiene(t.getEstado()))
                transEliminadas.add(Arrays.asList(t, e.getTransiciones()));
        
        /* Eliminamos las transiciones colgadas */
        for (List a : transEliminadas) {
            Transicion t = (Transicion) a.get(0);
            Conjunto<Transicion> c = (Conjunto<Transicion>) a.get(1);
            c.eliminar(t);
        }
    }
    
    /**
     * Realiza la copia de un autómata origen a otro de destino.
     * @param origen El autómata origen.
     * @param destino El autómata destino.
     */
    private static void copiarAutomata(Automata origen, Automata destino) {
        /* Copiamos los estados y transiciones */
        Automata.copiarEstados(origen, destino, 0);
        
        /* Copiamos los estados finales y las etiquetas */
        for (int i=0; i < origen.cantidadEstados(); i++) {
            Estado tmp = origen.getEstado(i);
            
            destino.getEstado(i).setEsFinal(tmp.getEsFinal());
            destino.getEstado(i).setEtiqueta(tmp.getEtiqueta());
        }
        
        /* Copiamos el alfabeto y la expresión regular */
        destino.setAlfabeto(origen.getAlfabeto());
        destino.setExprReg(origen.getExprReg());
    }

    /**
     * Determina si un grupo de estados tiene un estado final.
     * @param grupo Grupo de estados en el cual buscar el estado final.
     * @return <code>true</code> si el grupo de estados contiene un
     * estados final, <code>false</code> en caso contrario.
     */
    private static boolean tieneEstadoFinal(Conjunto<Estado> grupo) {
        for (Estado e : grupo)
            if (e.getEsFinal())
                return true;
        
        return false;
    }
    
    /**
     * Calcula la nueva etiqueta para un estado del nuevo AFD,
     * según los estados agrupados.
     * @param grupo Grupo de estados del AFD original.
     * @return La etiqueta para el estado del AFD nuevo.
     */
    private static String obtenerEtiqueta(Conjunto<Estado> grupo) {
        String etiqueta = "";
        String pedazo;
        
        for (Estado e : grupo) {
            /* Eliminamos la "i" o "f" en caso de que exista */
            if (e.toString().endsWith("i") || e.toString().endsWith("f"))
                pedazo = e.toString().substring(0, e.toString().length() - 1);
            else
                pedazo = e.toString();
            
            /* Agregamos */
            etiqueta += pedazo + " ";
        }
        
        if (etiqueta.endsWith(" "))
            etiqueta = etiqueta.substring(0, etiqueta.length() - 1);
        
        return "(" + etiqueta + ")";
    }
}