import java.io.File;

public class Logic2 {
    private String programName;
    private String pad;
    private int counter;     // Puedes usarlo para numerar secciones
    private int finalLOC;    // LOC totales de código
    private String finOut;   // Texto final para el reporte

    public Logic2(String programName, String pad) {
        this.programName = programName;
        this.pad = (pad == null) ? "" : pad;
    }

    public void logic2a(String inJavaFile, String outFile) {
        // 1) Leer
        Input input = new Input();
        String raw = input.readData(inJavaFile);

        // 2) Normalizar a arreglo de líneas
        Data data = new Data();
        String[] lines = data.saveData(raw);

        // 3) Contar LOC (sin comentarios/blanks)
        lineCounter lc = new lineCounter();
        int loc = lc.count(lines);

        // 4) Contar métodos
        methodCounter mc = new methodCounter();
        int methods = mc.count(lines);

        this.finalLOC = loc;

        // 5) Construir salida estilo PSP (simple, puedes adaptarlo a la tabla que pida tu 2a.pdf)
        String fileName = new File(inJavaFile).getName();
        StringBuilder out = new StringBuilder();
        out.append("Program: ").append(programName).append("\n");
        out.append("File: ").append(fileName).append("\n");
        out.append("--------------------------------------------------\n");
        out.append(String.format("%sLOC (code only): %d\n", pad, loc));
        out.append(String.format("%sMethods: %d\n", pad, methods));
        out.append("--------------------------------------------------\n");

        this.finOut = out.toString();

        // 6) Escribir a archivo y también a consola
        output outWriter = new output();
        outWriter.writeData(outFile, finOut);
        System.out.println(finOut);
    }
}
