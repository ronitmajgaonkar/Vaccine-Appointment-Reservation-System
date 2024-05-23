package scheduler.model;

import java.sql.SQLException;

public class Vaccine$VaccineBuilder {
    private final String vaccineName;
    private int availableDoses;

    public Vaccine$VaccineBuilder(String vaccineName, int availableDoses) {
        this.vaccineName = vaccineName;
        this.availableDoses = availableDoses;
    }

    public Vaccine build() throws SQLException {
        return new Vaccine(this);
    }
}
