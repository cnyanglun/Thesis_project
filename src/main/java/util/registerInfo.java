package util;

public class registerInfo implements java.io.Serializable{
    private String Account;
    private String password;
    private String email;

    public registerInfo(){

    }

    public registerInfo(String account, String password, String email) {
        Account = account;
        this.password = password;
        this.email = email;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
