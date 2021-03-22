package actions.iod;

import actions.ConcreteAction;
import structure.iod.OnDrawStructure;
import utils.CodeLocation;

public class IODNew  extends ConcreteAction {

    public IODNew(CodeLocation location) {
        super(location);
    }

    public void execute(OnDrawStructure onDrawMethod) {
        onDrawMethod.newInstance();
    }

    @Override
    public String generateBreakPoint() {
        return null;
    }
}
