package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class ACK_ERROR implements Operation {
    private String operationType;
    private short SubjectOpCode;
    private String info;
    private short shortCode;

    public ACK_ERROR(boolean isAck, short subjectOpCode, String info) {
        this.operationType = (isAck) ? "12" : "13";
        this.SubjectOpCode = subjectOpCode;
        this.info = info;
        this.shortCode = (short) ((isAck) ? 12 : 13);
    }

    public String OperationType() {
        return operationType;
    }

    public short getShortCode() {
        return shortCode;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setSubjectOpCode(short subjectOpCode) {
        SubjectOpCode = subjectOpCode;
    }

    public short getSubjectOpCode() {
        return SubjectOpCode;
    }

    public String getInfo() {
        return info;
    }
}
