import java.io.*;

public class Input {
    private String data;
    private BufferedReader br;

    public String readData(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            this.data = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo: " + file, e);
        } finally {
            try { if (br != null) br.close(); } catch (IOException ignored) {}
        }
        return data;
    }
}
