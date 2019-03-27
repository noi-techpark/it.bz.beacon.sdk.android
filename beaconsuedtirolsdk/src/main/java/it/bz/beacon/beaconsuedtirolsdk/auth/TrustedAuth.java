package it.bz.beacon.beaconsuedtirolsdk.auth;

public class TrustedAuth {
    private String username;
    private String password;

    public TrustedAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
