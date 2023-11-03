package users;

public class UserLoginData {

    private String email;
    private String password;

    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserLoginData withCorrectData(User user) {
        return new UserLoginData(user.getEmail(), user.getPassword());
    }

    public static UserLoginData withErrorEmail(User user) {
        return new UserLoginData(user.getEmail() + "errorData", user.getPassword());
    }

    public static UserLoginData withErrorPassword(User user) {
        return new UserLoginData(user.getEmail(), user.getPassword() + "errorData");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = password;
    }

}
