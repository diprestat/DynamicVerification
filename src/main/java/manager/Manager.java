package manager;

public interface Manager {
    void checkStructures();
    void generateCSV(String outputPath);
    String getBreakpoints();
    void execute(String key, String fileName, String lineNumber, String code, String id);
}
