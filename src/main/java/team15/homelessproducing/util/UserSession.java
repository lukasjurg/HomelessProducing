package team15.homelessproducing.util;

public class UserSession {
    private static UserSession instance;
    private Long currentUserId;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Long getCurrentUserId() {
        System.out.println("Current User ID: " + currentUserId); // Debugging log
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
