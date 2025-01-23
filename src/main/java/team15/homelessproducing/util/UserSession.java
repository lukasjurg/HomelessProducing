package team15.homelessproducing.util;

public class UserSession {
    private static UserSession instance;
    private Long currentUserId;
    private String currentUsername;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Long getCurrentUserId() {
        System.out.println("Fetching current user ID: " + currentUserId);
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        System.out.println("Setting current user ID to: " + currentUserId);
        this.currentUserId = currentUserId;
    }

    public String getCurrentUsername() {
        System.out.println("Fetching current username: " + currentUsername);
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        System.out.println("Setting current username to: " + currentUsername);
        this.currentUsername = currentUsername;
    }
}