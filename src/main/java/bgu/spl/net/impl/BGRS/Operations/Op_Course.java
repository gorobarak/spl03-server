package bgu.spl.net.impl.BGRS.Operations;

import bgu.spl.net.impl.BGRS.Operation;

public class Op_Course implements Operation {
    private String operationType;
    private String course;

    public Op_Course(Op_Course other){
        operationType = other.operationType;
        course = other.course;
    }

    public Op_Course() {
    }

    public String OperationType() {
        return operationType;
    }

    public String getCourse() {
        return course;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
