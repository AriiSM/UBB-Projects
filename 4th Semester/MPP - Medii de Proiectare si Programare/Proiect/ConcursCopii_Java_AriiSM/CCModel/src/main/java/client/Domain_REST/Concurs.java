package client.Domain_REST;

public class Concurs extends Entity<Integer> {
    private Categorie categorie;
    private Proba proba;

    public Concurs(Categorie categorie, Proba proba) {
        this.categorie = categorie;
        this.proba = proba;
    }

    public Concurs(){super();};

    @Override
    public Integer getId() {
        return super.getId();
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Proba getProba() {
        return proba;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return "Concurs {" +
                "categorie=" + categorie +
                ", proba=" + proba +
                '}';
    }
}
