package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class ACK_ERROR implements Operation {
    private String operationType;
    private String SubjectOpCode;
    private String info;

    public ACK_ERROR(boolean isAck, String subjectOpCode, String info) {
        this.operationType = (isAck) ? "12" : "13";
        this.SubjectOpCode = subjectOpCode;
        this.info = info;
    }

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

    public String getSubjectOpCode() {
        return SubjectOpCode;
    }

    public String getInfo() {
        return info;
    }
}
