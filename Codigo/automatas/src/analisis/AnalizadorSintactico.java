/*
 * Trabajo Práctico de Compiladores 2008.
 * 10mo Semestre Ingeniería Infomática.
 * Facultad Politécnica - UNA.
 */
package analisis;

/**
 *
 * @author Germán Hüttemann
 * @author Marcelo Rodas
 */
public class AnalizadorSintactico {
    
    /**
     * 
     */
    private AnalizadorLexico analizadorLexico;
    
    /**
     * 
     */
    private StringBuffer salida;
    
    /**
     * 
     */
    private Token preanalisis;
    
    /**
     * 
     */
    private int contadorTokens;
    
    /**
     * 
     * @param alfabeto
     * @param exprReg
     */
    public AnalizadorSintactico(Alfabeto alfabeto, String exprReg) {
        analizadorLexico = new AnalizadorLexico(alfabeto, exprReg);
        salida = new StringBuffer();
        contadorTokens = 0;
    }

    public String analizar() throws Exception {
        preanalisis = obtenerToken();
        
        if (preanalisis.getIdentificador() == TokenExprReg.FINAL)
            error("Expresión regular vacía");
        
        do {
            ExprReg();
        } while (preanalisis.getIdentificador() != TokenExprReg.FINAL);
        
        return salida.toString();
    }
    
    /**
     * Método que procesa las uniones de la expresión regular.
     * @throws java.lang.Exception Propaga la excepción de Concat().
     */
    private void ExprReg() throws Exception {
        Concat();
        
        while (true) {
            if (preanalisis.getIdentificador() == TokenExprReg.UNION) {
                match(preanalisis);
                Concat();
                escribir(new Token(TokenExprReg.UNION));
            }
            else {
                return;
            }
        }
    }

    /**
     * Método que procesa una concatenación en la expresión regular.
     * @throws java.lang.Exception Propaga la excepción de Grupo().
     */
    private void Concat() throws Exception {
        Grupo();
        
        while (true) {
            switch (preanalisis.getIdentificador()) {
                case PAREN_IZQ:
                case SIM_LEN:
                    Grupo();
                    escribir(new Token(TokenExprReg.CONCAT));
                    break;
                default:
                    return;
            }
        }
    }
    
    /**
     * Se elimina a R3 trasladando el vacío a Oper. De esta manera,
     * Oper deriva en vacío en caso de no existir un operador.
     * @throws java.lang.Exception Propaga las excepciones de Elem() y Oper().
     */
    private void Grupo() throws Exception {
        Elem();
        Oper();
    }
  
    /**
     * Método que procesa un elemento unitario en la expresión
     * regular. Se intenta hacer match con el paréntesis de
     * apertura o con algún símbolo del alfabeto. En caso 
     * contrario, se produce un error.
     * @throws java.lang.Exception En caso de que no se encuentre un símbolo
     * del alfabeto ni un paréntesis de apertura (inicio de una nueva expresión
     * regular).
     */
    private void Elem() throws Exception {
        switch (preanalisis.getIdentificador()) {
            case PAREN_IZQ:
                match(new Token(TokenExprReg.PAREN_IZQ));
                ExprReg();
                match(new Token(TokenExprReg.PAREN_DER));
                break;
            case SIM_LEN:
                SimLen();
                break;
            default:
                error("Se espera paréntesis de apertura o símbolo de alfabeto. " +
                    "Se encontró \"" + preanalisis.getValor() + "\"");
        }
    }
    
    /**
     * Método que procesa un operador en la expresión regular.
     * El vacío de R3 se traslada aquí. Se deriva en vacío si
     * no se encuentra alguno de los operadores.
     * @throws java.lang.Exception Propaga la excepción de match().
     */
    private void Oper() throws Exception {
        switch (preanalisis.getIdentificador()) {
            case CERO_MAS:
            case UNO_MAS :
            case CERO_UNO:
                escribir(preanalisis);
                match(preanalisis);
                break;
            default:
                return;
        }
    }
    
    /**
     * Método que procesa un símbolo del alfabeto en la expresión regular.
     * @throws java.lang.Exception Si el caracter actual no es un símbolo del alfabeto.
     */
    private void SimLen() throws Exception {
        if (analizadorLexico.getAlfabeto().contiene(preanalisis.getValor())) {
            escribir(preanalisis);
            match(preanalisis);
        }
        else {
            error("El símbolo \"" + preanalisis.getValor() + 
                "\" no pertenece al alfabeto definido.");
        }
    }

    /**
     * 
     * @param entrada
     * @throws java.lang.Exception
     */
    private void match(Token entrada) throws Exception {
        if (preanalisis.equals(entrada))
            preanalisis = obtenerToken();
        else if (entrada.getIdentificador() == TokenExprReg.PAREN_DER)
            error("Falta paréntesis de cierre");
        else
            error("Token con valor inválido");
    }
    
    /**
     * Método que escribe la cadena de salida de la traducción.
     * @param entrada Token a escribir.
     */
    private void escribir(Token entrada) {
        salida.append(entrada.getValor());
    }
    
    private void error(String mensaje) throws Exception {
        String mensajeCompleto = "";
        
        mensajeCompleto += "Error de sintáxis\n";
        mensajeCompleto += "Carácter: " + preanalisis.getValor() + "\n";
        mensajeCompleto += "Posición: " + contadorTokens + "\n";
        mensajeCompleto += "Mensaje : " + mensaje;
        
        throw new Exception(mensajeCompleto);
    }
    
    private Token obtenerToken() {
        ++contadorTokens;
        return analizadorLexico.sgteToken();
    }
}