package manager;

import actions.dw.DWAcquire;
import actions.dw.DWRelease;
import structure.dw.WakeLockStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class DWManager implements Manager {
    private HashMap<String, DWAcquire> acquires;
    private HashMap<String, DWRelease> releases;
    private HashMap<String, WakeLockStructure> structures;

    public DWManager() {
        this.acquires = new HashMap<String, DWAcquire>();
        this.releases = new HashMap<String, DWRelease>();
        this.structures = new HashMap<String, WakeLockStructure>();
    }

    public void addAcquire(String key, DWAcquire acquire) {
        this.acquires.put(key, acquire);
    }

    public void addRelease(String key, DWRelease release) {
        this.releases.put(key, release);
    }

    public void executeAcquire(String key, String id) {
        this.structures.put(id, this.acquires.get(key).execute(id));
    }

    public void executeRelease(String key, String id) {
        this.releases.get(key).execute(this.structures.get(id));
    }

    public void checkStructures() {
        for (java.util.Map.Entry<String, WakeLockStructure> stringStructureEntry : this.structures.entrySet()) {
            HashMap.Entry<String, WakeLockStructure> pair = (HashMap.Entry) stringStructureEntry;
            pair.getValue().checkStructure();
        }
    }

    public void generateCSV() {
        File csvOutputFile = new File("test_DW.csv");
        try (PrintWriter writer = new PrintWriter(csvOutputFile)) {
            writer.write("apk, package, file, method\n");
            for (java.util.Map.Entry<String, WakeLockStructure> stringStructureEntry : this.structures.entrySet()) {
                HashMap.Entry<String, WakeLockStructure> pair = (HashMap.Entry) stringStructureEntry;
                if (pair.getValue().hasCodeSmell()) {
                    String fileName = pair.getValue().getLocation().getFileName();
                    String methodName = pair.getValue().getLocation().getMethodName();
                    writer.write("apk,package,"+fileName+","+methodName+"\n");
                }
            }
        } catch (FileNotFoundException e) {
            // Do something
        }
    }

    @Override
    public String getBreakpoints() {
        //Todo
        return null;
    }
}