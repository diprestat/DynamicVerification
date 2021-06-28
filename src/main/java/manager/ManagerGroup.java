package manager;

import java.io.*;

public class ManagerGroup {
    public HMUManager managerHMU;
    public DWManager managerDW;
    public IODManager managerIOD;
    public HPManager managerHP;
    public NLMRManager managerNLMR;

    public ManagerGroup() {
        this.managerDW = new DWManager();
        this.managerHMU = new HMUManager();
        this.managerIOD = new IODManager();
        this.managerHP = new HPManager();
        this.managerNLMR = new NLMRManager();
    }

    public void checkStructures() {
        this.managerHMU.checkStructures();
        this.managerDW.checkStructures();
        this.managerIOD.checkStructures();
        this.managerHP.checkStructures();
        this.managerNLMR.checkStructures();
    }

    public void generateCSV(String outputPath, String apkName, String packageName, boolean returnAllInstances) throws IOException {
        //Print for Coverage
        if (returnAllInstances) {
            File coverageOutputfile = new File(outputPath + "coverage.csv");
            try (PrintWriter writer = new PrintWriter(coverageOutputfile)) {
                writer.write("");
            } catch (FileNotFoundException e) {
                // Do something
            }
            File executionOutputfile = new File(outputPath + "execution.csv");
            try (PrintWriter writer = new PrintWriter(executionOutputfile)) {
                writer.write("");
            } catch (FileNotFoundException e) {
                // Do something
            }
        }

        this.managerDW.generateCSV(outputPath, apkName, packageName, returnAllInstances);
        this.managerHMU.generateCSV(outputPath, apkName, packageName, returnAllInstances);
        this.managerIOD.generateCSV(outputPath, apkName, packageName, returnAllInstances);
        this.managerHP.generateCSV(outputPath, apkName, packageName, returnAllInstances);
        this.managerNLMR.generateCSV(outputPath, apkName, packageName, returnAllInstances);
    }

    public void execute(String key, String fileName, String lineNumber, String code, String id) {
        managerDW.execute(key, fileName, lineNumber, code, id);
        managerHMU.execute(key, fileName, lineNumber, code, id);
        managerIOD.execute(key, fileName, lineNumber, code, id);
        managerHP.execute(key, fileName, lineNumber, code, id);
        managerNLMR.execute(key, fileName, lineNumber, code, id);
    }
}
