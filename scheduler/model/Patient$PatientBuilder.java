package scheduler.model;

public class Patient$PatientBuilder {
    private final String username;
    private final byte[] salt;
    private final byte[] hash;

    public Patient$PatientBuilder(String username, byte[] salt, byte[] hash) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
    }

    public Patient build() {
        return new Patient(this);
    }
}
