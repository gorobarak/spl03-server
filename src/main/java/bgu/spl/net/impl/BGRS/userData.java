package bgu.spl.net.impl.BGRS;

import java.util.Set;

public class userData {
    private String password;
    private boolean isAdmin;
    private boolean isLoggedIn = false;
    private Set<String> courses; //TODO should it be ordered as was in courses.txt?


    public userData(String password, boolean isAdmin){
        this.password = password;
        this.isAdmin = isAdmin;

    }

    public String getPassword() {
        return password;
    }

    public Set<String> getCourses() {
        return courses;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}

