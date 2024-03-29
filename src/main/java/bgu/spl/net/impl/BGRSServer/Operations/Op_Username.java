package bgu.spl.net.impl.BGRSServer.Operations;

import bgu.spl.net.impl.BGRSServer.Operation;

public class Op_Username implements Operation {
    private String operationType;
    private String username;


    public Op_Username(Op_Username other){
        operationType = other.operationType;
        username = other.username;
    }

    public Op_Username() {
    }

    public String OperationType() {
        return operationType;
    }

    public String getUsername() {
        return username;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Op_Username{" +
                "operationType='" + operationType + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
