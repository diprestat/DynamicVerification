package staticanalyzis;

import events.iod.IODEnter;
import events.iod.IODExit;
import events.iod.IODNew;
import manager.IODManager;
import manager.ManagerGroup;
import soot.*;
import structure.OnDrawStructure;
import utils.CodeLocation;

import java.util.regex.Matcher;

public class IODAnalyzer extends CodeSmellAnalyzer {

    public static void checkLine(String line, String name, String methodName, int lineNumber, ManagerGroup managerGroup, Body b, Unit u, UnitPatchingChain units, boolean isInstrumenting) {
        checkNew(line, name, methodName,lineNumber, managerGroup.managerIOD, b, u, b.getUnits(), isInstrumenting);
    }

    public static void checkNew(String line, String name, String methodName, int lineNumber, IODManager manager, Body b, Unit u, UnitPatchingChain units, boolean isInstrumenting) {
        Matcher m = findPattern(line, "<init>");
        //Matcher m2 = findPattern(methodName, "onDraw");
        if (checkMethodName(methodName,"onDraw") && m.find()) {
            String variableName=m.group(0).split("\\.")[0];
            if (!getStructureInstanceLocalName(line).equals("refBuilder")) {
                manager.addNew(generateKey(name, methodName), new IODNew(new CodeLocation(name, methodName, lineNumber)));
                if (isInstrumenting) {
                    buildInstrumentation(getStructureInstanceLocalName(line), units, u, b, "iodnew:");
                }
            }
        }
    }

    public static void methodsToCheck(String name, String methodName, int lineNumber, ManagerGroup managerGroup, Body b, UnitPatchingChain units, boolean isInstrumenting) {
        checkIOD(name, methodName, "onDraw", lineNumber, managerGroup.managerIOD, b, b.getUnits(),isInstrumenting);
    }

    protected static void checkIOD(String name, String methodName, String methodNameNeeded, int lineNumber, IODManager manager, Body b, UnitPatchingChain units, boolean isInstrumenting) {
        if (checkMethodName(methodName, methodNameNeeded)) {
            String key=generateKey(name, methodName);
            manager.addStructure(name+"$onDraw", new OnDrawStructure(new CodeLocation(name, methodName, lineNumber), name));
            manager.addEnter(key, new IODEnter(new CodeLocation(name, methodName, lineNumber)));
            manager.addExit(key, new IODExit(new CodeLocation(name, methodName, lineNumber)));
        }
        buildMethod(methodName, methodNameNeeded, b, b.getUnits(), "iodenter:", "iodexit:", isInstrumenting);
    }
}
