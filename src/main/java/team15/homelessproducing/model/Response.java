package team15.homelessproducing.model;

public class Response {
    private String message;
    private String role;
    private Long userId;
    private String username;

    public Response(String message, String role, Long userId, String username) {
        this.message = message;
        this.role = role;
        this.userId = userId;
        this.username = username;
        System.out.println("Response created: Message=" + message + ", Role=" + role + ", UserId=" + userId + ", Username=" + username);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}