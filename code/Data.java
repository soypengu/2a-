public class Data {
    // Normaliza saltos de línea y separa en arreglo
    public String[] saveData(String data) {
        if (data == null) return new String[0];
        String normalized = data.replace("\r\n", "\n").replace("\r", "\n");
        return normalized.split("\n", -1);
    }
}
