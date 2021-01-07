package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class Op_Username_Password implements Operation {
    private String operationType;
    private String username;
    private String password;

    public Op_Username_Password(Op_Username_Password other){
        operationType = other.operationType;
        username = other.username;
        password = other.password;
    }

    public Op_Username_Password() {
    }

    public String OperationType() {
        return operationType;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Op_Username_Password{" +
                "operationType='" + operationType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
