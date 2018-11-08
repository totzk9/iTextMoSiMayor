package gte.com.itextmosimayor.SMS;

public interface SMSListener {
        /**
         * To call this method when new message received and send back
         * @param message Message
         */
        void messageReceived(String message);
}