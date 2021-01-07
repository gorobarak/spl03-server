package bgu.spl.net.impl.BGRS;

import java.util.ArrayList;
import java.util.List;


public class userData {
    private String password;
    private boolean isAdmin;
    private boolean isLoggedIn = false;
    private List<String> courses; //TODO should it be ordered as was in courses.txt? - YES


    public userData(String password, boolean isAdmin){
        this.password = password;
        this.isAdmin = isAdmin;
        courses = new ArrayList<>();

    }

    public String getPassword() {
        return password;
    }

    public List<String> getCourses() {
        return courses;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void login() {
        isLoggedIn = true;
    }

    public void logout() {
        isLoggedIn = false;
    }

}

