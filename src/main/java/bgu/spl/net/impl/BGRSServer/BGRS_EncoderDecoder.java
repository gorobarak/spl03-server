package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.Operations.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class BGRS_EncoderDecoder implements MessageEncoderDecoder<Operation> {
    private final byte[] operationBuffer = new byte[2];
    private int operationIndex = 0;
    private Operation operation = null;
    private HashMap<Short, String> opcodes = new HashMap<Short, String>() {{
        put((short) 1,"ADMINREG");
        put((short) 2,"STUDENTREG");
        put((short) 3,"LOGIN");
        put((short) 4,"LOGOUT");
        put((short) 5,"COURSEREG");
        put((short) 6,"KDAMCHECK");
        put((short) 7,"COURSESTAT");
        put((short) 8,"STUDENTSTAT");
        put((short) 9,"ISREGISTERED");
        put((short) 10,"UNREGISTER");
        put((short) 11,"MYCOURSES");
        put((short) 12,"ACK");
        put((short) 13,"ERROR");
    }};
    private byte[] buffer = new byte[1 << 10];
    private int len = 0;
    private short opcode;
    private int counter = -1;

    @Override
    public Operation decodeNextByte(byte nextByte) {
        if (operation == null) { //indicates that we are still reading the op code
            operationBuffer[operationIndex] = nextByte;
            operationIndex++;
            if (operationIndex == operationBuffer.length) { //we read 2 bytes and therefore can construct the appropriate operation
                operationIndex = 0;
                opcode = bytesToShort(operationBuffer);
                String op = opcodes.get(opcode);
                switch (opcode){
                    case 1 : case 2 : case 3 :
                        operation = new Op_Username_Password();
                        ((Op_Username_Password)operation).setOperationType(op);
                        counter = 2;
                        break;
                    case 4 : case 11 :
                        operation = new Op();
                        ((Op)operation).setOperationType(op);
                        Operation result = fillFields(operation);
                        operation = null; //rest the operation
                        return result;
                    case 8 :
                        operation = new Op_Username();
                        ((Op_Username)operation).setOperationType(op);
                        counter = 1;
                        break;
                    case 5 : case 6 : case 7: case 9 : case 10 :
                        operation = new Op_Course();
                        ((Op_Course)operation).setOperationType(op);

                        break;
                }
            }
        }
        else {
            pushByte(nextByte);
            if (len == 2 && (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 9 || opcode == 10)){ // in case the message doesn't end with '\0'
                Operation result = fillFields(operation);
                operation = null; //rest the operation
                return result;
            }
            if (nextByte == '\0'){
                counter--;
            }
            if(counter == 0) {
                Operation result = fillFields(operation);
                operation = null; //rest the operation
                return result;
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Operation message) {
        byte[] code = shortToBytes(((ACK_ERROR) message).getShortCode());
        byte[] subjectCode = shortToBytes(((ACK_ERROR) message).getSubjectOpCode());
        byte[] info = ((ACK_ERROR) message).getInfo().getBytes();//UTF-8 by default
        return setMessage(code, subjectCode, info);
    }

    private byte[] setMessage(byte[] code, byte[] subjectCode, byte[] info) {
        byte[] out = new byte[info.length + 5]; //2+2+1
        out[0] = code[0];
        out[1] = code[1];
        out[2] = subjectCode[0];
        out[3] = subjectCode[1];
        for (int i = 0; i < info.length; i++) {
            out[i + 4] = info[i];
        }
        out[info.length+4] = '\0';
        return out;
    }

    private void pushByte(byte nextByte) {
        if (len >= buffer.length) {
            buffer = Arrays.copyOf(buffer, len * 2);
        }
        buffer[len] = nextByte;
        len++;
    }

    private Operation fillFields(Operation operation){
        Operation result;
        switch (opcode){
            case 1 : case 2 : case 3 :
                result = new Op_Username_Password((Op_Username_Password) operation);
                int delimiter = find0();
                ((Op_Username_Password)result).setUsername(new String(buffer, 0, delimiter, StandardCharsets.UTF_8));
                ((Op_Username_Password)result).setPassword(new String(buffer, delimiter+1,len-delimiter-2 , StandardCharsets.UTF_8));
                break;
            case 4 : case 11 :
                result = new Op((Op)operation);
                break;
            case 8 :
                result = new Op_Username((Op_Username) operation);
                ((Op_Username)result).setUsername(new String(buffer, 0, len-1, StandardCharsets.UTF_8));
                break;
            default:
                result = new Op_Course((Op_Course) operation);
                ((Op_Course)result).setCourse(String.valueOf(bytesToShort(buffer)));
                break;
        }
        len = 0;
        return result;
    }

    private int find0() {
        for (int i = 0; i < len; i++) {
            if (buffer[i]== '\0'){
                return i;
            }
        }
        return 0;
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

}
