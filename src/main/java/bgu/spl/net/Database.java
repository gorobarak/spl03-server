package bgu.spl.net;


/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {


	/**
	 * synchronized - only one writer at a time
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean registerAdmin(String username, String password) {
	}

	/**
	 * synchronized - only one writer at a time
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean registerStudent(String username, String password) {
	}

	/**
	 * synchronized - only one writer at a time
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean login(String username, String password) {
	}

	public boolean isRegistered(String username) {//should check both admins and students
	}

	public boolean isValidUser(String username, String password) {
	}

	public boolean isLoggedIn(String username) {
	}

	public boolean isAdmin(String username) {
	}

	public void logout(String username) {
	}



	/**
	 *
	 * @param courseNum
	 * @return  a list of all the kdams ordered according to the order in Course.txt
	 */
	public String kdam(String courseNum) {
	}


	public String getCourseStat(String courseNum) {
	}

	/**
	 *
	 * @param courseNum
	 * @param username
	 * @return "REGISTERED" or "NOT REGISTERED"
	 */
	public String isRegisteredToCourse(String courseNum, String username) {
	}

	/**
	 *  doesn't have to be synchronised because it's guarantied that only one client is logged in to a user TODO validate that statement
	 * errors:
	 * no such course exist
	 * already registered to the course
	 * no available slots
	 * doesn't have all the kdams!!!
	 * @param courseNum
	 * @param username
	 * @return true iff successful
	 */
	public boolean registerToCourse(String courseNum, String username) {
	}

	/**
	 * doesn't have to be synchronised because it's guarantied that only one client is logged in to a user TODO validate that statement
	 * @param courseNum
	 * @param username
	 */
	public void unregisterFromCourse(String courseNum, String username) {
	}

	public String getStudentStat(String studentName) {
	}

	public String getMyCourses(String username) {
	}

	private static class InstanceHolder{
		private static Database instance = new Database();
	}
	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return InstanceHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		// TODO: implement
		return false;
	}


}
