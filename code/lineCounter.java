public class lineCounter {
    public int totalLines;        // LOC de código (sin comentarios/blank)
    public String[] arrData;

    public int count(String[] arrData) {
        this.arrData = arrData;
        boolean inBlockComment = false; // /* ... */
        int loc = 0;

        for (String raw : arrData) {
            String line = raw;
            if (line == null) line = "";
            // Elimina espacios laterales
            line = line.trim();
            if (line.isEmpty()) {
                continue; // línea en blanco
            }

            StringBuilder codeOnly = new StringBuilder();
            int i = 0;
            while (i < line.length()) {
                if (!inBlockComment) {
                    // Inicio de comentario de bloque
                    if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i += 2;
                        continue;
                    }
                    // Comentario de línea //
                    if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break; // ignora el resto
                    }
                    // Manejo básico de strings para no confundir // dentro de comillas
                    if (line.charAt(i) == '"' || line.charAt(i) == '\'') {
                        char quote = line.charAt(i);
                        codeOnly.append(line.charAt(i++));
                        while (i < line.length()) {
                            codeOnly.append(line.charAt(i));
                            if (line.charAt(i) == quote && line.charAt(i - 1) != '\\') {
                                i++;
                                break;
                            }
                            i++;
                        }
                        continue;
                    }
                    // Normal: es código
                    codeOnly.append(line.charAt(i));
                    i++;
                } else {
                    // Estamos dentro de /* ... */
                    if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i += 2;
                    } else {
                        i++;
                    }
                }
            }

            String stripped = codeOnly.toString().trim();
            if (!stripped.isEmpty()) {
                loc++;
            }
        }

        this.totalLines = loc;
        return loc;
    }
}
