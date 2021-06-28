package manager;

import events.dw.DWAcquire;
import events.dw.DWRelease;
import events.hp.HPEnter;
import events.hp.HPExit;
import structure.hp.HeavyProcessStructure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HPManager implements Manager{

    private HashMap<String, HPEnter> enters; // Key = CodeLocation
    private HashMap<String, HPExit> exits; // Key = CodeLocation
    private HashMap<String, HeavyProcessStructure> structures;

    public HPManager() {
        this.enters = new HashMap<String, HPEnter>();
        this.exits = new HashMap<String, HPExit>();
        this.structures = new HashMap<String, HeavyProcessStructure>();
    }

    @Override
    public void checkStructures() {
        for (java.util.Map.Entry<String, HeavyProcessStructure> stringStructureEntry : this.structures.entrySet()) {
            HashMap.Entry<String, HeavyProcessStructure> pair = (HashMap.Entry) stringStructureEntry;
            pair.getValue().checkStructure();
        }
    }

    @Override
    public void generateCSV(String outputPath, String apkName, String packageName, boolean returnAllInstances) throws IOException {
        File directory = new File(outputPath);
        if (! directory.exists()){
            directory.mkdir();
        }

        //Print for coverage
        if (returnAllInstances) {
            File coverageOutputfile = new File(outputPath + "coverage.csv");
            try (PrintWriter writer = new PrintWriter(new FileWriter(coverageOutputfile, true))) {
                writer.write("Number of HP methods," + this.enters.size() + "\n");
            } catch (FileNotFoundException e) {
                // Do something
            }

            File executionOutputFile = new File(outputPath + "execution.csv");
            try (PrintWriter writer = new PrintWriter(new FileWriter(executionOutputFile, true))) {
                int executionSumMethod=0;
                for (Map.Entry<String, HPEnter> executioncountEntry : this.enters.entrySet()) {
                    if (executioncountEntry.getValue().isExecuted) {
                        executionSumMethod++;
                    }
                }

                writer.write("Number of HP methods," + executionSumMethod + "\n");
            } catch (FileNotFoundException e) {
                // Do something
            }
        }

        File csvOutputFile = new File(outputPath+"results_HP.csv");
        try (PrintWriter writer = new PrintWriter(csvOutputFile)) {
            writer.write("apk,package,file,method,average executing time,worst executing time\n");
            for (java.util.Map.Entry<String, HeavyProcessStructure> stringStructureEntry : this.structures.entrySet()) {
                HashMap.Entry<String, HeavyProcessStructure> pair = (HashMap.Entry) stringStructureEntry;
                if (pair.getValue().hasCodeSmell()) {
                    String fileName = pair.getValue().getLocation().getFileName();
                    String methodName = pair.getValue().getLocation().getMethodName();
                    writer.write(apkName+ ","+ packageName +","+fileName+","+methodName+ ","+pair.getValue().getAverageTime()+","+pair.getValue().getWorstTime()+"\n");
                }
            }
        } catch (FileNotFoundException e) {
            // Do something
        }

        if (returnAllInstances) {
            File csvOutputFileAll = new File(outputPath + "results_HP_all.csv");
            try (PrintWriter writer = new PrintWriter(csvOutputFileAll)) {
                writer.write("apk,package,file,method,average executing time,worst executing time\n");
                for (java.util.Map.Entry<String, HeavyProcessStructure> stringStructureEntry : this.structures.entrySet()) {
                    HashMap.Entry<String, HeavyProcessStructure> pair = (HashMap.Entry) stringStructureEntry;
                    String fileName = pair.getValue().getLocation().getFileName();
                    String methodName = pair.getValue().getLocation().getMethodName();
                    writer.write(apkName + "," + packageName + "," + fileName + "," + methodName + "," + pair.getValue().getAverageTime() + "," + pair.getValue().getWorstTime() + "\n");
                }
            } catch (FileNotFoundException e) {
                // Do something
            }
        }
    }

    @Override
    public void execute(String key, String fileName, String lineNumber, String code, String id) {
        if ("hasenter".equals(code)) {
            //key = key.replace("$onPostExecute", "").replace("$onPreExecute","").replace("onProgressUpdate", "");
            fileName = fileName.replace("$onPostExecute", "").replace("$onPreExecute","").replace("$onProgressUpdate", "");
            executeEnter(key, fileName, Long.parseLong(id));
        } else if ("hasexit".equals(code)) {
            //key = key.replace("$onPostExecute", "").replace("$onPreExecute","").replace("onProgressUpdate", "");
            fileName = fileName.replace("$onPostExecute", "").replace("$onPreExecute","").replace("$onProgressUpdate", "");
            executeExit(key, fileName, Long.parseLong(id));
        } else if ("hbrenter".equals(code)) {
            executeEnter(key, fileName.replace("$onReceive",""), Long.parseLong(id));
        } else if ("hbrexit".equals(code)) {
            executeExit(key, fileName.replace("$onReceive",""), Long.parseLong(id));
        } else if ("hssenter".equals(code)) {
            executeEnter(key, fileName.replace("$onStartCommand",""), Long.parseLong(id));
        } else if ("hssexit".equals(code)) {
            executeExit(key, fileName.replace("$onStartCommand",""), Long.parseLong(id));
        }
    }

    public void addEnter(String key, HPEnter enter) {
        this.enters.put(key, enter);
    }

    public void addExit(String key, HPExit exit) {
        this.exits.put(key, exit);
    }

    public void addStructure(String key, HeavyProcessStructure structure) {this.structures.put(key, structure);}

    public void executeEnter(String key, String id, long date) {
        //this.structures.put(id, this.enters.get(key).execute(id, date));
/*
        System.out.println("Need : " + key + " and " + id);
        for (java.util.Map.Entry<String, HPEnter> stringStructureEntry : this.enters.entrySet()) {
            HashMap.Entry<String, HPEnter> pair = (HashMap.Entry) stringStructureEntry;
            System.out.println("Enter : " + pair.getKey());
        }

        for (java.util.Map.Entry<String, HeavyProcessStructure> stringStructureEntry : this.structures.entrySet()) {
            HashMap.Entry<String, HeavyProcessStructure> pair = (HashMap.Entry) stringStructureEntry;
            System.out.println("Structure : " + pair.getKey() + " " + pair.getValue().getId());
        }
*/
        this.enters.get(key).execute(this.structures.get(id), date);
    }

    public void executeExit(String key, String id, long date) {
        this.exits.get(key).execute(this.structures.get(id), date);
    }

}
