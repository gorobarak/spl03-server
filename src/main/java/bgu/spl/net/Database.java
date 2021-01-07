package bgu.spl.net;

import bgu.spl.net.impl.BGRSServer.courseData;
import bgu.spl.net.impl.BGRSServer.userData;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;// Import this class to handle errors
import java.util.*;


/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private List<String> order;//courses order
	private Map<String,courseData> courses ; // courseNum - courseData
	private Map<String,userData> users; // username - userData
	//private Map<String, List<String>> studentToCoursesMap; // coursesNum list is ordered as in 'Courses.txt'
	private Map<String,List<String>> courseToStudentsMap; // student list is ordered alphabetically
	private final Object lockA = new Object();

	public boolean isCourse(String courseNum) {
		return courses.containsKey(courseNum);
	}

	private static class InstanceHolder{
		private static Database instance = new Database();
	}
	//to prevent user from creating new Database
	private Database() {
		courses = new HashMap<>();
		users = new HashMap<>();
		//studentToCoursesMap = new HashMap<>();
		courseToStudentsMap = new HashMap<>();
		order = new ArrayList<>();
		initialize("Courses.txt");
		sortKdams();// kdams in courseData objects is sorted


	}

	private void sortKdams() {
			for (Map.Entry<String, courseData> e : courses.entrySet()){
				e.getValue().getKdams().sort((a,b)->{ return order.indexOf(a)-order.indexOf(b); }); //TODO: validate
			}
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		//System.out.println("getInstance()"); //TODO delete
		return InstanceHolder.instance;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		courseData course;
		try{
			File file = new File(coursesFilePath);
			Scanner myReader = new Scanner(file);
			int counter = 0;
			while(myReader.hasNextLine()){
				course = parseToCourse(myReader.nextLine());
				courses.put(course.getCourseNum(),course);
				order.add(course.getCourseNum());
				courseToStudentsMap.put(course.getCourseNum(),new ArrayList<>()); //initialize courses in map
			}
			myReader.close();

		}catch (FileNotFoundException e){
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private courseData parseToCourse(String nextLine) {
		String[] data = nextLine.split("\\|"); //3|Algebra|[]|12 -> ["3","Algebra","[]","12"]
		List<String> kdams;
		if(data[2].equals("[]")){ //kdams are empty
			kdams = new ArrayList<>();
		}else {
			String[] pre_kdams = data[2].substring(1, data[2].length() - 1).split(",");//"[]" = ('[',']')
			kdams = new ArrayList<>(Arrays.asList(pre_kdams));
		}
		int available_max_slots = Integer.parseInt(data[3]);
		String courseNum = data[0];
		String courseName = data[1];
		return new courseData(available_max_slots,available_max_slots,courseNum,courseName,kdams);
	}

	/**
	 * synchronized A - only one writer at a time
	 * when reached inside check again that username isn't registered already
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean registerAdmin(String username, String password) {
		synchronized (lockA) {
			if (!isRegistered(username)) {
				userData u = new userData(password, true);
				users.put(username, u);
				return true;
			}
			return false;
		}
	}

	/**
	 * synchronized A - only one writer at a time
	 * when reached inside check again that username isn't registered already
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean registerStudent(String username, String password) {
		//out.println("outside registerStudent()");
		synchronized (lockA) {
			//System.out.println("registerStudent()"); //TODO delete
			if (!isRegistered(username)) {
				userData u = new userData(password, false);
				users.put(username, u);
				return true;
			}
			return false;
		}
	}

	/**
	 * synchronized on username - only one writer at a time
	 * before getting here, we already checked: username is registered and valid, local client is not logged in
	 * check: user isn't logged in
	 * @param username
	 * @param password
	 * @return true iff successful
	 */
	public boolean login(String username, String password) {
		synchronized (users.get(username)) {
			if (isLoggedIn(username)) {
				return false;
			}//else
			users.get(username).login();
			return true;
		}
	}

	public boolean isRegistered(String username) {//should check both admins and students
		return users.containsKey(username);
	}

	public boolean isValidUser(String username, String password) {
		return (users.containsKey(username)) && password.equals(users.get(username).getPassword());
	}

	public boolean isLoggedIn(String username) {
		return users.containsKey(username) && users.get(username).isLoggedIn();
	}

	public boolean isAdmin(String username) {
		return users.containsKey(username) && users.get(username).isAdmin();
	}

	public void logout(String username) {
		users.get(username).logout();
	}

	/**
	 *
	 * @param courseNum
	 * @return  a list of all the kdams ordered according to the order in Courses.txt
	 */
	public String kdam(String courseNum) {
		if (!courses.containsKey(courseNum))
			return null;
		return courses.get(courseNum).getKdams().toString().replace(" ",""); //removes spaces
	}

	/**
	 * Synchronized B - reading data that isn't fully changed
	 * @param courseNum
	 * @return
	 */
	public String getCourseStat(String courseNum) {
		synchronized (courses.get(courseNum)) {
			courseData c = courses.get(courseNum);
			return ("Course: (" + courseNum + ") " + c.getCourseName() + "\n" +
					"Seats Available: " + c.getAvailableSlots() + "/" + c.getMaxSlots() + "\n" +
					"Students registered: " + courseToStudentsMap.get(courseNum).toString());
		}

	}

	/**
	 * Synchronized B - reading half baked data
	 * @param studentName
	 * @return
	 */
	public String getStudentStat(String studentName) {
		synchronized (users.get(studentName)) {
			userData u = users.get(studentName);
			return ("Student: " + studentName + "\n" +
					"Courses: " + u.getCourses().toString());//TODO remove spaces?
		}
	}

	private boolean hasKdams(String courseNum, String username) {
		userData user = users.get(username);
		courseData course = courses.get(courseNum);
		System.out.println("kdams = " + course.getKdams()); //todo delete
		System.out.println("kdamsSize " + course.getKdams().size());
		for (String kdam : course.getKdams()) {
			if (!user.getCourses().contains(kdam)) {
				System.out.println(kdam); //todo delete
				return false;
			}
		}
		return true;
	}


	/**
	 * Synchronized B - reading data that isn't fully changed
	 * doesn't have to be synchronised because it's guarantied that only one client is logged in to a user TODO validate that statement
	 * errors:
	 * already registered to the course
	 * no available slots
	 * doesn't have all the kdams!!!
	 * @param courseNum
	 * @param username
	 * @return true iff successful
	 */
	public boolean registerToCourse(String courseNum, String username) {
		synchronized (users.get(username)) {
			synchronized (courses.get(courseNum)) {
				userData user = users.get(username);
				courseData course = courses.get(courseNum);
				System.out.println("alreadyRegistered = " + user.getCourses().contains(courseNum)); //todo delete
				System.out.println("availableSlots = " + course.getAvailableSlots());
				System.out.println("hasKdmas = " + hasKdams(courseNum,username));
				if (user.getCourses().contains(courseNum) || course.getAvailableSlots() == 0 || !hasKdams(courseNum,username)) {
					return false;
				}//else register student to course:
				user.getCourses().add(courseNum);
				user.getCourses().sort((a,b)->{ return order.indexOf(a)-order.indexOf(b); });
				course.addStudent();
				courseToStudentsMap.get(courseNum).add(username);
				courseToStudentsMap.get(courseNum).sort(Comparator.naturalOrder());
				return true;
			}
		}
	}


	/**
	 * Synchronized B - reading data that isn't fully changed
	 * @param courseNum
	 * @param username
	 */
	public void unregisterFromCourse(String courseNum, String username) {
		synchronized (users.get(username)) {
			synchronized (courses.get(courseNum)) {
				userData user = users.get(username);
				courseData course = courses.get(courseNum);
				user.getCourses().remove(courseNum);//remove the course from the student's courses
				course.removeStudent(); //increase available slots
				courseToStudentsMap.get(courseNum).remove(username); //remove the student from the list of registered students
				//TODO is sort kept?
			}
		}
	}

	/**
	 *
	 * @param courseNum
	 * @param username
	 * @return "REGISTERED" or "NOT REGISTERED"
	 */
	public String isRegisteredToCourse(String courseNum, String username) {
		if (users.get(username).getCourses().contains(courseNum)){
			return "REGISTERED";
		}
		return "NOT REGISTERED";
	}


	public String getMyCourses(String username) {
		return (users.get(username).getCourses().toString().replace(" ",""));
	}








}
