package gte.com.itextmosimayor.models;

import java.io.Serializable;

public class MessagesData implements Serializable {

    private String messageID;
    private String dateSent;
    private String clientMobileNumber;
    private String content;
    private String mayorID;
    private String priorityLevel;
    private String isAssigned;
    private String status;
    private String departmentName;
    private String convoID;

    public MessagesData (String messageID, String dateSent, String clientMobileNumber, String content, String mayorID,
                           String priorityLevel, String isAssigned, String status) {
        this.messageID = messageID;
        this.dateSent = dateSent;
        this.clientMobileNumber = clientMobileNumber;
        this.content = content;
        this.mayorID = mayorID;
        this.priorityLevel = priorityLevel;
        this.isAssigned = isAssigned;
        this.status = status;
    }

    public MessagesData (String messageID, String dateSent, String clientMobileNumber, String content, String mayorID,
                         String priorityLevel, String isAssigned, String status, String departmentName) {
        this.messageID = messageID;
        this.dateSent = dateSent;
        this.clientMobileNumber = clientMobileNumber;
        this.content = content;
        this.mayorID = mayorID;
        this.priorityLevel = priorityLevel;
        this.isAssigned = isAssigned;
        this.status = status;
        this.departmentName = departmentName;
    }

    public MessagesData (String messageID, String dateSent, String clientMobileNumber, String content, String mayorID,
                         String priorityLevel, String isAssigned, String status, String departmentName, String convoID) {
        this.messageID = messageID;
        this.dateSent = dateSent;
        this.clientMobileNumber = clientMobileNumber;
        this.content = content;
        this.mayorID = mayorID;
        this.priorityLevel = priorityLevel;
        this.isAssigned = isAssigned;
        this.status = status;
        this.departmentName = departmentName;
        this.convoID = convoID;
    }

    public MessagesData (String messageID, String dateSent, String clientMobileNumber, String content) {
        this.messageID = messageID;
        this.dateSent = dateSent;
        this.clientMobileNumber = clientMobileNumber;
        this.content = content;
    }

    public String getConvoID() {
        return convoID;
    }

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

    public String getDepartmentName() {
        return departmentName;
    }
}
