package models;

public class User {
    private final String id;
    private final String name;
    private final String role;
    private final String password;

    public User(String id, String name, String role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
}
