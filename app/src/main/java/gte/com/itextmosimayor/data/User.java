package gte.com.itextmosimayor.data;

public class User {
    private static String FirstName, LastName, MobileNumber, Username, Password, DepartmentID, MayorID;
    public User (String firstName, String lastName, String mobileNumber, String username,
                 String password, String departmentID, String mayorID){
        FirstName = firstName;
        LastName = lastName;
        MobileNumber = mobileNumber;
        Username = username;
        Password = password;
        DepartmentID = departmentID;
        MayorID = mayorID;
    }

    protected static String getFirstName(){
        return FirstName;
    }

    public String getLastName(){
        return LastName;
    }

    public String getMobileNumber(){
        return MobileNumber;
    }

    public String getUsername(){
        return Username;
    }

    public String getPassword(){
        return Password;
    }

    public String getDepartmentID(){
        return DepartmentID;
    }

    public String MayorID(){
        return MayorID;
    }
}
