package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class Op implements Operation {
    private String operationType;

    public Op(Op other){
        operationType = other.operationType;
    }

    public Op() {
    }

    public String OperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
