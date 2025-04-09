package ppd.common;

import java.util.List;

// Clasa CompetitorBlock reprezintă un obiect din blocul de concurenți care este trimis.
public class CompetitorBlock {
    private final String countryName;
    private final List<int[]> competitors;  // ID si SCORE

    public CompetitorBlock(String countryName, List<int[]> competitors) {
        this.countryName = countryName;
        this.competitors = competitors;
    }

    // Getters and setters
    public String getCountryName() {
        return countryName;
    }

    public List<int[]> getCompetitors() {
        return competitors;
    }

}
