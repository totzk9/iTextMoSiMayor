package gte.com.itextmosimayor.constant;

public interface Constants {

    // PREFERENCES
    String MYPREFERENCES = "MyPREFERENCES";
    String ISLOGIN = "islogin";
    String USERNAME = "username";
    String USERPASS = "password";

    //LOGIN
    String SIGNUPNOWLABEL = "Not a member? " + "<b>" + "Sign Up Now!" + "</b> ";

    //MESSAGING
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 10;
    int READ_SMS_PERMISSIONS_REQUEST = 1;
    int MY_PERMISSIONS_REQUEST_READ_SMS = 99;

    //URLS
    String URL = "http://dev2-mayor.mybudgetload.com:8080/mayor.asp?";
    String REGISTER = "cmd=register";
    String USERLOGIN = "cmd=logIn";
    String FETCHUNASSIGNEDMESSAGE = "cmd=fetchMessageUnassigned";
    String FETCHOPENMESSAGE = "cmd=fetchMessageOpen";
    String FETCHIMPORTANTMESSAGE = "cmd=fetchMessageImportant";
    String FETCHRESOLVEDMESSAGE = "cmd=fetchMessageResolved";
    String FETCHUNASSIGNEDMESSAGETHREAD = "cmd=fetchUnassignedMessageThread";
    String FETCHDEPARTMENTS = "cmd=fetchDepartments";
    String UPDATEMESSAGETORESOLVED = "cmd=updateMessageToResolved";
    String ISEXISTINGCODE = "cmd=isExistingCode";
    String ISVALIDCODE = "cmd=isValidCode";
    String ADDDEPARTMENT = "cmd=addDepartment";
    String ASSIGNMESSAGE = "cmd=assignMessage";



}
