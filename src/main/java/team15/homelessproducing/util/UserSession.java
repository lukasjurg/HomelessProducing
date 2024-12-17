package team15.homelessproducing.util;

public class UserSession {
    private static UserSession instance;
    private Long currentUserId;

    // Private constructor to enforce singleton pattern
    private UserSession() {}

    // Static method to get the singleton instance
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getter for current user ID
    public Long getCurrentUserId() {
        System.out.println("Current User ID: " + currentUserId); // Debugging log
        return currentUserId;
    }

    // Setter for current user ID
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
