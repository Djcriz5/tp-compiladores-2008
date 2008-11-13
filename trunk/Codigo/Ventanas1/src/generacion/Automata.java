/*
 * Trabajo Práctico de Compiladores 2008.
 * 10mo Semestre Ingeniería Infomática.
 * Facultad Politécnica - UNA.
 */
package generacion;

import analisis.Alfabeto;

/**
 * Clase que representa la abstracción para un Autómata Finito,
 * sea Determinístico (AFD) o No determinístico (AFN).<br><br>
 * Inicialmente, un AFN es contruído a partir de una expresión
 * regular a través de las construcciones de Thompson.<br><br>
 * Un AFD es generado a partir de un AFN a través del algoritmo
 * de Subconjuntos.<br><br>
 * Adicionalmente, un AFD puede ser reducido (en cuanto a
 * cantidad de estados se refiere) a través del algoritmo
 * de Minimización de Estados.
 * @author Germán Hüttemann
 * @author Marcelo Rodas
 */
public abstract class Automata {
    
    /**
     * Conjunto de estados del autómata.
     */
    protected Conjunto<Estado> estados;
    
    /**
     * Expresión regular para este autómata.
     */
    protected String exprReg;
    
    /**
     * Alfabeto para este autómata.
     */
    protected Alfabeto alfabeto;
    
    /**
     * Constructor por defecto.
     */
    protected Automata() {
       this(null, "");
    }
    
    /**
     * Construye un <code>Automata</code> con un determinado <code>Alfabeto</code>
     * y una determinada expresión regular.
     * @param alfabeto El <code>Alfabeto</code> de este <code>Automata</code>.
     * @param exprReg La expresión regular para este <code>Automata</code>.
     */
    protected Automata(Alfabeto alfabeto, String exprReg) {
        estados = new Conjunto<Estado>();
        setAlfabeto(alfabeto);
        setExprReg(exprReg);
    }
    
    /**
     * Obtiene el <code>Alfabeto</code> de este <code>Automata</code>.
     * @return El <code>Alfabeto</code> de este <code>Automata</code>.
     */
    public Alfabeto getAlfabeto() {
        return alfabeto;
    }

    /**
     * Establece el <code>Alfabeto</code> de este <code>Automata</code>.
     * @param alfabeto El nuevo <code>Alfabeto</code> para este <code>Automata</code>.
     */
    public void setAlfabeto(Alfabeto alfabeto) {
        this.alfabeto = alfabeto;
    }

    /**
     * Obtiene la expresión regular para este <code>Automata</code>.
     * @return La expresión regular para este <code>Automata</code>.
     */
    public String getExprReg() {
        return exprReg;
    }

    /**
     * Establece la expresión regular para este <code>Automata</code>.
     * @param exprReg La nueva expresión regular para este <code>Automata</code>.
     */
    public void setExprReg(String exprReg) {
        this.exprReg = exprReg;
    }
    
    /**
     * Obtiene el <code>Estado</code> inicial del <code>Automata</code>.
     * @return El <code>Estado</code> inicial del <code>Automata</code>.
     */
    public Estado getEstadoInicial() {
        return estados.obtenerPrimero();
    }
    
    /**
     * Obtiene los <code>Estado</code>s finales del <code>Automata</code>.
     * @return El conjunto de <code>Estado</code>s finales del <code>Automata</code>.
     */
    public Conjunto<Estado> getEstadosFinales() {
        Conjunto<Estado> finales = new Conjunto<Estado>();
        
        for (Estado tmp : estados)
            if (tmp.getEsFinal())
                finales.agregar(tmp);
        
        return finales;
    }
    
    /**
     * Obtiene los <code>Estado</code>s no finales del <code>Automata</code>.
     * @return El conjunto de <code>Estado</code>s no finales del <code>Automata</code>.
     */
    public Conjunto<Estado> getEstadosNoFinales() {
        Conjunto<Estado> noFinales = new Conjunto<Estado>();
        
        for (Estado tmp : estados)
            if (!tmp.getEsFinal())
                noFinales.agregar(tmp);
        
        return noFinales;
    }
    
    /**
     * Agrega un <code>Estado</code> al <code>Automata</code>.
     * @param estado Nuevo <code>Estado</code> para el <code>Automata</code>.
     */
    public void agregarEstado(Estado estado) {
        estados.agregar(estado);
    }
    
    /**
     * Recupera un determinado <code>Estado</code> del <code>Automata</code>.
     * @param pos El identificador del <code>Estado</code> a recuperar.
     * @return El <code>Estado</code> recuperado.
     */
    public Estado getEstado(int pos) {
        return estados.obtener(pos);
    }
    
    /**
     * Recupera el conjunto de <code>Estado</code>s del <code>Automata</code>.
     * @return El conjunto de <code>Estado</code>s el <code>Automata</code>.
     */
    public Conjunto<Estado> getEstados() {
        return estados;
    }
    
    /**
     * Recupera la cantidad de estados del <code>Automata</code>.
     * @return Cantidad de estados del <code>Automata</code>.
     */
    public int cantidadEstados() {
        return estados.cantidad();
    }
    
    /**
     * Establece a <code>false</code> el estado de visitado de todos los 
     * <code>Estado</code>s de este <code>Automata</code>. Útil para
     * iniciar un recorrido nuevo sobre los <code>Estado</code>s de este
     * <code>Automata</code>.
     */
    public void iniciarRecorrido() {
        for (Estado tmp : estados)
            tmp.setVisitado(false);
    }
    
    @Override
    public String toString() {
        String str = "";
        
        for (Estado tmp : getEstados()) {
            str += tmp.toString();
            
            for (Transicion trans : tmp.getTransiciones())
                str += " --> " + trans.getEstado() + "(" + trans.getSimbolo() + ")";
            
            str += "\n";
        }
        
        return str;
    }
    
    /**
     * Copia los estados de un autómata a otro.
     * @param afOrigen Automata desde el cual copiar estados.
     * @param afDestino Automata hacia el cual copiar estados.
     * @param incremento Cantidad en la cual deben incrementarse los identificadores
     * de los estados finales de las transiciones.
     */
    public static void copiarEstados(Automata afOrigen, Automata afDestino, int incremento) {
        copiarEstados(afOrigen, afDestino, incremento, 0);
    }
    
    /**
     * Copia los estados de un autómata a otro, omitiendo una cantidad
     * determinada del autómata de origen.
     * @param afOrigen Automata desde el cual copiar estados.
     * @param afDestino Automata hacia el cual copiar estados.
     * @param incrementoTrans Cantidad en la cual deben incrementarse los identificadores
     * de los estados finales de las transiciones.
     * @param omitidos Cantidad de estados de <code>origen</code> que deben ser omitidos.
     */
    public static void copiarEstados(Automata afOrigen, Automata afDestino, 
                    int incrementoTrans, int omitidos) {
        
        /* 
         * Cantidad que hay que incrementar al identificador
         * de un estado de afnOrigen para convertirlo en el
         * correspondiente estado de afnDestino.
         */
        int incrementoEst = incrementoTrans; //afnDestino.cantidadEstados(); TODO
        
        /* Agregamos los nuevos estados para afnDestino */
        for (int i=omitidos; i < afOrigen.cantidadEstados(); i++)
            afDestino.agregarEstado(new Estado(afDestino.cantidadEstados()));
        
        /* Contador de omitidos */
        int contador = 0;
        
        /* Agregamos las transiciones de cada estado */
        for (Estado tmp : afOrigen.getEstados()) {
            
            if (omitidos > contador++)
                continue;
            
            /* Estado de afnDestino al cual se agregarán las transiciones */
            Estado objetivo = afDestino.getEstado(tmp.getIdentificador() + incrementoEst);
            
            /* Para cada estado, agregamos las transiciones */
            copiarTransiciones(afDestino, tmp.getTransiciones(), objetivo, incrementoTrans);
        }
    }
    
    /**
     * 
     * @param afDestino
     * @param transiciones
     * @param objetivo
     * @param incrementoTrans
     */
    public static void copiarTransiciones(Automata afDestino, Conjunto<Transicion> transiciones, 
                        Estado objetivo, int incrementoTrans) {
        
        for (Transicion trans : transiciones) {
            Integer idDestino = trans.getEstado().getIdentificador();
            String simbolo = trans.getSimbolo();

            Estado estadoDestino = afDestino.getEstado(idDestino + incrementoTrans);
            Transicion nuevaTrans = new Transicion(estadoDestino, simbolo);

            objetivo.getTransiciones().agregar(nuevaTrans);
        }
    }
}
