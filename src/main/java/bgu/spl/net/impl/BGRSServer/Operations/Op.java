package bgu.spl.net.impl.BGRSServer.Operations;

import bgu.spl.net.impl.BGRSServer.Operation;

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

    @Override
    public String toString() {
        return "Op{" +
                "operationType='" + operationType + '\'' +
                '}';
    }
}
