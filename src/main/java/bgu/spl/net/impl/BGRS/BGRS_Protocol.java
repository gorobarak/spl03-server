package bgu.spl.net.impl.BGRS;

import bgu.spl.net.Database;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRS.Operations.ACK_ERROR;
import bgu.spl.net.impl.BGRS.Operations.Op_Course;
import bgu.spl.net.impl.BGRS.Operations.Op_Username;
import bgu.spl.net.impl.BGRS.Operations.Op_Username_Password;

public class BGRS_Protocol implements MessagingProtocol<Operation> {
    boolean loginState = false;
    Database database = Database.getInstance();
    boolean isAdmin = false;
    boolean shouldTerminate = false;
    String username;

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    @Override
    public Operation process(Operation msg) {
        String operationType = msg.OperationType();
        switch (operationType){
            case "ADMINREG":
                return adminreg(msg);
            case "STUDENTREG":
                return studentreg(msg);
            case "LOGIN":
                return login(msg);
            case "LOGOUT":
                return logout(msg);
            case "COURSEREG":
                return coursereg(msg);
            case "KDAMCHECK":
                return kdamcheck(msg);
            case "COURSESTAT":
                return coursestat(msg);
            case "STUDENTSTAT":
                return studentstat(msg);
            case "ISREGISTERED":
                return isregistered(msg);
            case "UNREGISTER":
                return unregister(msg);
            default://case "MYCOURSES":
                return mycourses(msg);

        }
    }




    //TODO can we register from a logged in account - NO
    /**
     * opcode 1
     * errors: already logged in, already registered,
     * otherwise: register user
     * @param msg
     * @return
     */
    private Operation adminreg(Operation msg) {
        String username = ((Op_Username_Password)msg).getUsername();
        String password = ((Op_Username_Password)msg).getPassword();
        if (loginState || database.isRegistered(username) || !database.registerAdmin(username, password)) {
            return new ACK_ERROR(false, "1", "");
        }
        return new ACK_ERROR(true, "1", "");
    }

    //TODO can a username and an admin share same details
    /**
     * opcode 2
     * errors: already logged in, already registered,
     * otherwise: register user
     * @param msg
     * @return
     */
    private Operation studentreg(Operation msg) {
        String username = ((Op_Username_Password)msg).getUsername();
        String password = ((Op_Username_Password)msg).getPassword();
        if (loginState || database.isRegistered(username) || !database.registerStudent(username, password)) {
            return new ACK_ERROR(false, "2", "");
        }
        return new ACK_ERROR(true, "2", "");
    }

    /**
     * opcode 3
     * @errors:
     * loginState == true
     * not registered
     * wrong password
     * already logged in from another client
     * otherwise: changes loginState to true locally and at database
     * @param msg
     * @return answer
     */
     private Operation login(Operation msg){
         username = ((Op_Username_Password)msg).getUsername();
         String password = ((Op_Username_Password)msg).getPassword();
         if (loginState || !database.isValidUser(username, password) || database.isLoggedIn(username) || !database.login(username, password)) {
             return new ACK_ERROR(false, "3", "");
         }
         loginState = true;
         if (database.isAdmin(username)) {
             isAdmin = true;
         }
         return new ACK_ERROR(true, "3", "");
     }

    /**
     * opcode 4
     * errors: not logged in
     * otherwise: logout user
     * @param msg
     * @return
     */
    private Operation logout(Operation msg) {
        if (!loginState) {
            return new ACK_ERROR(false, "4", "");
        }
        loginState = false;
        isAdmin = false;
        database.logout(username);
        shouldTerminate = true;
        return new ACK_ERROR(true, "4", "");
    }

    /**
     * opcode 5
     * errors:
     * not logged in
     * user is admin
     * registerToCourse errors
     * @param msg
     * @return
     */
    private Operation coursereg(Operation msg) {
        String courseNum = ((Op_Course)msg).getCourse();
        if (!loginState || isAdmin || !database.registerToCourse(courseNum, username)) {
            return new ACK_ERROR(false, "5", "");
        }
        return new ACK_ERROR(true, "5", "");

    }

    /**
     * opcode 6
     * errors:
     * //TODO should we check if the user is logged in?
     * @param msg
     * @return
     */
    private Operation kdamcheck(Operation msg) {
        String courseNum = ((Op_Course)msg).getCourse();
        return new ACK_ERROR(true,"6",database.kdam(courseNum));
    }

    /**
     * opcode 7
     * errors:check if the course exists
     * @param msg
     * @return
     */
    private Operation coursestat(Operation msg) {
        String courseNum = ((Op_Course)msg).getCourse();
        if (!loginState || !isAdmin || !database.isCourse(courseNum)) {
            return new ACK_ERROR(false, "7", "");
        }
        return new ACK_ERROR(true, "7", database.getCourseStat(courseNum));
    }

    /**
     * opcode 8
     * errors: not logged in, not admin, student not registered
     * otherwise: register user
     * @param msg
     * @return
     */
    private Operation studentstat(Operation msg) {
        String studentName = ((Op_Username)msg).getUsername();
        if (!isAdmin || !database.isRegistered(studentName)) {
            return new ACK_ERROR(false, "8", "");
        }
        return new ACK_ERROR(true, "8", database.getStudentStat(studentName));
    }

    /**
     * opcode 9
     * errors: not logged in, am admin
     * otherwise: "REGISTERED" or "NOT REGISTERED"
     * @param msg
     * @return
     */
    private Operation isregistered(Operation msg) {
        String courseNum = ((Op_Course)msg).getCourse();
        if (!loginState || isAdmin) {
            return new ACK_ERROR(false, "9", "");
        }
        return new ACK_ERROR(true, "9", database.isRegisteredToCourse(courseNum, username));
    }

    /**
     * opcode 10
     * errors: not logged in, am admin, not registered to course
     * otherwise: unregister student from course
     * @param msg
     * @return
     */
    private Operation unregister(Operation msg) {
        String courseNum = ((Op_Course)msg).getCourse();
        if (!loginState || isAdmin || database.isRegisteredToCourse(courseNum, username).equals("NOT REGISTERED")) {
            return new ACK_ERROR(false, "10", "");
        }
        database.unregisterFromCourse(courseNum, username);
        return new ACK_ERROR(true, "10", "");

    }

    /**
     * opcode 11
     * errors: not logged in, is admin,
     * otherwise: register user
     * @param msg
     * @return
     */
    private Operation mycourses(Operation msg) {
        if (!loginState || isAdmin) {
            return new ACK_ERROR(false, "11", "");
        }
        return new ACK_ERROR(true, "11", database.getMyCourses(username));
    }
}
