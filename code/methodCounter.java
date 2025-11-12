import java.util.regex.*;

public class methodCounter {
    public int totalMethods;
    public String[] arrData;

    // Excluye estructuras de control (if, for, while, switch, catch, else, do, try, synchronized)
    private static final Pattern METHOD_SIGNATURE = Pattern.compile(
        // Modificadores opcionales
        "^(?:\\s*(?:public|protected|private|static|final|native|synchronized|abstract|transient|strictfp)\\s+)*" +
        // Tipo de retorno (o nombre de clase si es constructor)
        "(?:[\\w\\<\\>\\[\\]]+\\s+)?"+
        // Nombre
        "([A-Za-z_\\$][\\w\\$]*)\\s*" +
        // Paréntesis de parámetros
        "\\(.*\\)\\s*" +
        // Opcional throws
        "(?:throws\\s+[\\w\\.,\\s]+)?\\s*" +
        // Abre llave opcional (puede estar en la sgte línea)
        "\\{?\\s*$"
    );

    private static final Pattern CONTROL_STRUCTURES = Pattern.compile(
        "^(?:if|for|while|switch|catch|else|do|try|synchronized)\\b"
    );

    public int count(String[] arrData) {
        this.arrData = arrData;
        int methods = 0;

        boolean inBlockComment = false;
        for (int idx = 0; idx < arrData.length; idx++) {
            String raw = arrData[idx];
            if (raw == null) continue;
            String line = raw;

            // Quitar comentarios de bloque y de línea (simplificado, igual que en lineCounter)
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < line.length()) {
                if (!inBlockComment) {
                    if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true; i += 2; continue;
                    }
                    if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break;
                    }
                    sb.append(line.charAt(i++));
                } else {
                    if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false; i += 2; continue;
                    }
                    i++;
                }
            }

            String stripped = sb.toString().trim();
            if (stripped.isEmpty()) continue;

            // Excluir estructuras de control
            if (CONTROL_STRUCTURES.matcher(stripped).find()) continue;

            // Heurística: línea que parece cabecera de método
            Matcher m = METHOD_SIGNATURE.matcher(stripped);
            if (m.find()) {
                // Para evitar contar declaraciones de clase/interfaz/enum
                if (stripped.matches("^(class|interface|enum)\\b.*")) continue;
                // Si no tiene '{' en esta línea, valida si la siguiente abre el bloque
                if (!stripped.contains("{")) {
                    // Mira la siguiente línea no vacía/comentario
                    int j = idx + 1;
                    while (j < arrData.length && arrData[j].trim().isEmpty()) j++;
                    if (j < arrData.length && arrData[j].trim().startsWith("{")) {
                        methods++;
                        continue;
                    }
                } else {
                    methods++;
                }
            }
        }

        this.totalMethods = methods;
        return methods;
    }
}
