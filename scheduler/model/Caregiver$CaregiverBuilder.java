package scheduler.model;

public class Caregiver$CaregiverBuilder {
    private final String username;
    private final byte[] salt;
    private final byte[] hash;

    public Caregiver$CaregiverBuilder(String username, byte[] salt, byte[] hash) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
    }

    public Caregiver build() {
        return new Caregiver(this);
    }
}
