import java.io.*;

public class output {
    public void writeData(String outFile, String outText) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            bw.write(outText);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo salida: " + outFile, e);
        }
    }
}
