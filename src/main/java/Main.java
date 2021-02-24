import actions.hmu.HMUAddition;
import actions.hmu.HMUImplementation;
import instrumentation.AndroidInstrument;
import staticanalyzis.Analyzer;
import staticanalyzis.SootAnalyzer;
import utils.CodeLocation;
import utils.HMUManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Main {



    public static void main(String[] args) throws IOException {

        HMUManager manager;
        //manager = classicAnalyzer();
        String platformPath = "android-platforms";
        String apkPath = "tests_apks/app-debug.apk";
        manager = sootAnalyzer(platformPath, apkPath);
        /*
        String tracePath = "tests_ressources/trace_test_1.txt";
        analyzeTrace(manager, tracePath);
         */
    }

    public static HMUManager classicAnalyzer() throws IOException {
        HMUManager manager = new HMUManager();
        String path = "tests_folders/LambdaApp";
        Analyzer test = new Analyzer();

        test.analyze(manager, path);
        System.out.print(manager.getBreakpoints());
        return manager;
    }

    public static HMUManager sootAnalyzer(String platformPath, String apkPath) {
        HMUManager manager = new HMUManager();
        SootAnalyzer test = new SootAnalyzer(platformPath, apkPath);
        test.analyze(manager);
        return manager;
    }

    public static void analyzeTrace(HMUManager manager, String tracePath) {
        BufferedReader reader;
        int traceNumberLine=1;
        try {
            reader = new BufferedReader(new FileReader(
                    tracePath));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                String[] result = line.split(":");
                if (result.length==4) {
                    //System.out.println("[TOCHECK]" + traceNumberLine + " " + line);
                    String fileName = result[0];
                    String lineNumber = result[1];
                    String id = result[3];
                    String key = fileName + ":" + lineNumber;
                    if ("impl".equals(result[2])) {
                        manager.executeImplementation(key, id);
                    } else if ("add".equals(result[2])) {
                        manager.executeAddition(key, id);
                    } else if ("del".equals(result[2])) {
                        manager.executeDeletion(key, id);
                    } else if ("cln".equals(result[2])) {
                        manager.executeDeletion(key, id);
                    }
                }
                else {
                    //System.out.println("[AVOID]" + traceNumberLine + " " + line);
                }
                line = reader.readLine();
                traceNumberLine++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        manager.checkStructures();
    }

}
