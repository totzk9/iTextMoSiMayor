package gte.com.itextmosimayor.models;

//@Entity(tableName = DBInfo.UnassignedMessage)
public class UnassignedMessagesData {

//    @PrimaryKey(autoGenerate = true)
//    private int _id;

    private String messageID;
    private String dateSent;
    private String clientMobileNumber;
    private String content;
    private String mayorID;
    private String priorityLevel;
    private String isAssigned;
    private String status;

    public UnassignedMessagesData(String messageID, String dateSent, String clientMobileNumber, String content, String mayorID, String priorityLevel, String isAssigned, String status) {
        this.messageID = messageID;
        this.dateSent = dateSent;
        this.clientMobileNumber = clientMobileNumber;
        this.content = content;
        this.mayorID = mayorID;
        this.priorityLevel = priorityLevel;
        this.isAssigned = isAssigned;
        this.status = status;
    }

//    public void set_id(int _id) {
//        this._id = _id;
//    }

    public String getMessageID() {
        return messageID;
    }

    public String getDateSent() {
        return dateSent;
    }

    public String getClientMobileNumber() {
        return clientMobileNumber;
    }

    public String getContent() {
        return content;
    }

    public String getMayorID() {
        return mayorID;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public String getIsAssigned() {
        return isAssigned;
    }

    public String getStatus() {
        return status;
    }

//    public int get_id() {
//        return _id;
//    }
}