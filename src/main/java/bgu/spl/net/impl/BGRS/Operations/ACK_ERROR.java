package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class ACK_ERROR implements Operation {
    private String operationType;
    private String SubjectOpCode;
    private String info;

    public String OperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setSubjectOpCode(String subjectOpCode) {
        SubjectOpCode = subjectOpCode;
    }
}
