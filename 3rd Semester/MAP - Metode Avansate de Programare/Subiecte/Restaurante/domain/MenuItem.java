package com.example.restaurante.domain;

public class MenuItem extends Entity<Long>{
    private String category;
    private String item;
    private Float price;
    private String currency;

    public MenuItem(Long aLong, String category, String item, Float price, String currency) {
        super(aLong);
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public String getItem() {
        return item;
    }

    public Float getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "category='" + category + '\'' +
                ", item='" + item + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
