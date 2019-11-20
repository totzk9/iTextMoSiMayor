package gte.com.itextmosimayor.database;

import android.provider.BaseColumns;

public class DatabaseInfo {

    public static class DBInfo implements BaseColumns {

        static final String _ID = "_id INTEGER PRIMARY KEY AUTOINCREMENT, ";

        //TableName
        static final String User = "user";
        public static final String Message = "message";
//        public static final String Mayor = "mayor";
        public static final String Department = "department";
//        public static final String Department_Address = "department_address";
//        public static final String Department_Code = "department_code";
//        public static final String Convo = "convo";
        static final String Convo_Content = "convo_content";
        static final String Convo_Kuan = "convo_kuan";


        //subs
        static final String UnassignedMessage = "unassigned_message";
        static final String OpenMessage = "open_message";
        static final String ResolvedMessage = "resolved_message";
        static final String ImportantMessage = "important_message";
        static final String AllMessage = "all_message";
        static final String DeletedMessage = "deleted_message";
        static final String ConfidentialMessage = "confidential_message";


        //USER Fields
        public static final String USERID = "UserID";
        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";
        public static final String FIRSTNAME = "FirstName";
        public static final String LASTNAME = "LastName";
        public static final String MOBILENUMBER = "MobileNumber";
        //include DEPARTMENTID
        //include MAYORID

        //MESSAGE Fields
        public static final String MESSAGEID = "MessageID";
        public static final String DATESENT = "DateSent";
        static final String CLIENTMOBILENUMBER = "ClientMobileNumber";
        public static final String CONTENT = "Content";
        //include MAYORID
        static final String PRIORITYLEVEL = "PriorityLevel";
        static final String ISASSIGNED = "isAssigned";
        static final String STATUS = "Status";

        //MAYOR Fields
        public static final String MAYORID = "MayorID";
//        public static final String DATEELECTED = "DateElected";
//        public static final String ISADMIN = "IsAdmin";
//        public static final String MAYORCODE = "MayorCode";

        //DEPARTMENT Fields
        public static final String DEPARTMENTID = "DepartmentID";
        public static final String DEPARTMENTNAME = "DepartmentName";
        public static final String DEPARTMENTHEAD = "DepartmentHead";
//        public static final String DEPARTMENTADDRESS = "DepartmentAddress";
        //include DEPARTMENTCODE

        //DEPARTMENT_ADDRESS Fields
//        public static final String ADDRESSID = "AddressID";
        public static final String STREETNAME = "StreetName";
        public static final String BARANGAY = "Barangay";
        public static final String MUNICIPALITY = "Municipality";
        public static final String PROVINCE = "Province";
        public static final String ZIPCODE = "ZipCode";
        //include DEPARTMENTID
        //include DEPARTMENTCODE

        //DEPARTMENT_CODE Fields
//        public static final String DEPARTMENTCODEID = "DepartmentCodeID";
        public static final String DEPARTMENTCODE = "DepartmentCode";

        //CONVO Fields
        static final String CONVOID = "ConvoID";
        //include MESSAGEID
        //include DEPARTMENTID

        //CONVO_CONTENT Fields
        static final String CONTENTID = "ContentID";
        //include CONVOID
        public static final String MESSAGECONTENT = "MessageContent";
        //include DATESENT
        public static final String SENTBY = "SentBy";


    }
}
