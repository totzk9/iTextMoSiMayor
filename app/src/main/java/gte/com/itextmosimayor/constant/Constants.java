package gte.com.itextmosimayor.constant;

public interface Constants {

    //Default
    String DATABASE_NAME = "itextmosimayor_app";
    String DEFAULTIMGURL = "https://firebasestorage.googleapis.com/v0/b/itextmosimayor.appspot.com/o/imguploads%2Fdefault.jpeg?alt=media&token=d39af29e-eaaa-4c84-8e80-43610da4a2f3";
    // PREFERENCES
    String MYPREFERENCES = "MyPREFERENCES";
    String ISLOGIN = "islogin";
    String USERNAME = "username";
    String USERPASS = "password";

    //LOGIN
    String SIGNUPNOWLABEL = "Not a member? " + "<b>" + "Sign Up Now!" + "</b> ";

    //MESSAGING
    int READ_SMS_PERMISSIONS_REQUEST = 1;

    //URLS
    String URL = "http://dev2-mayor.mybudgetload.com:8383/mayor.asp?";
    String REGISTER = "cmd=register";
    String USERLOGIN = "cmd=logIn";
    String FETCHUNASSIGNEDMESSAGE = "cmd=fetchMessageUnassigned";
    String FETCHOPENMESSAGE = "cmd=fetchMessageOpen";
    String FETCHIMPORTANTMESSAGE = "cmd=fetchMessageImportant";
    String FETCHRESOLVEDMESSAGE = "cmd=fetchMessageResolved";
    String FETCHDELETEDMESSAGE = "cmd=fetchMessageDeleted";
    String FETCHDELETEDMESSAGETHREAD = "cmd=fetchDeletedMessageThread";
    String FETCHUNASSIGNEDMESSAGETHREAD = "cmd=fetchUnassignedMessageThread";
    String FETCHDEPARTMENTS = "cmd=fetchDepartments";
    String UPDATEMESSAGETORESOLVED1 = "cmd=updateMessageToResolved1";
    String UPDATEMESSAGETORESOLVED2 = "cmd=updateMessageToResolved2";
    String ISEXISTINGCODE = "cmd=isExistingCode";
    String ISVALIDCODE = "cmd=isValidCode";
    String ADDDEPARTMENT = "cmd=addDepartment";
    String ASSIGNMESSAGE = "cmd=assignMessage";
    String ASSIGNMESSAGE2 = "cmd=assignMessage2";
    String FETCHASSIGNEDMESSAGE = "cmd=fetchAssignedMessage";
    String FETCHIMPORTANTASSIGNEDMESSAGE = "cmd=fetchImportantAssignedMessage";
    String FETCHRESOLVEDASSIGNEDMESSAGE = "cmd=fetchResolvedAssignedMessage";
    String INSERTCONVO = "cmd=insertConvo";
    String FETCHCONVOBYDEPARTMENT = "cmd=fetchConvoByDepartment";
    String DELETEMESSAGE = "cmd=deleteMessage";
    String FETCHCONFIDENTIALMESSAGE = "cmd=fetchConfidentialMessage";
    String FETCHCONFIDENTIALMESSAGETHREAD = "cmd=fetchConfidentialMessageThread";
    String MOVETOCONFIDENTIAL = "cmd=moveToConfidential";
    String GETSERVERTIME = "cmd=getDateTime";
}