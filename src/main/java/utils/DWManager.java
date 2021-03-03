package utils;

import actions.dw.DWAcquire;
import actions.dw.DWImplementation;
import actions.dw.DWRelease;
import structure.dw.WakeLockStructure;
import structure.hmu.MapStructure;

import java.util.HashMap;

public class DWManager {
    private HashMap<String, DWImplementation> implementations;
    private HashMap<String, DWAcquire> acquires;
    private HashMap<String, DWRelease> releases;
    private HashMap<String, WakeLockStructure> structures;

    public DWManager() {
        this.implementations = new HashMap<String, DWImplementation>();
        this.acquires = new HashMap<String, DWAcquire>();
        this.releases = new HashMap<String, DWRelease>();
        this.structures = new HashMap<String, WakeLockStructure>();
    }

    public void addImplementation(String key, DWImplementation implementation) {
        this.implementations.put(key, implementation);
    }

    public void addAcquire(String key, DWAcquire acquire) {
        this.acquires.put(key, acquire);
    }

    public void addRelease(String key, DWRelease release) {
        this.releases.put(key, release);
    }

    public void executeImplementation(String key, String id) {
        this.structures.put(id, this.implementations.get(key).execute(id));
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
}