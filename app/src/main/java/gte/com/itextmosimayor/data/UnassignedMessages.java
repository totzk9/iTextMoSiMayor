package gte.com.itextmosimayor.data;

public class UnassignedMessages {
    private String DateSent, ClientMobileNumber, Content, PriorityLevel, Status;
    private int MessageID, MayorID, AssignedToID;
    public UnassignedMessages(int messageID, String dateSent, String clientMobileNumber, String content,
                              int mayorID, String priorityLevel, int assignedToID, String status) {
        this.MessageID = messageID;
        this.DateSent = dateSent;
        this.ClientMobileNumber = clientMobileNumber;
        this.Content = content;
        this.MayorID = mayorID;
        this.PriorityLevel = priorityLevel;
        this.AssignedToID = assignedToID;
        this.Status = status;
    }

    public int getMessageID() {
        return MessageID;
    }

    public String getDateSent(){
        return DateSent;
    }

    public String getClientMobileNumber(){
        return ClientMobileNumber;
    }

    public String getContent(){
        return Content;
    }

    public int getMayorID() {
        return MessageID;
    }

    public String getPriorityLevel() {
        return PriorityLevel;
    }

    public int getAssignedToID(){
        return AssignedToID;
    }

    public String getStatus() {
        return Status;
    }
}
