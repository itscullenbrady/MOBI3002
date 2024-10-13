package com.codelab.basics;

public class DataModel {

    private long item_id;
    private String name;
    private String description;
    private int quantity;
    private String type;
    public int access_count;
//
//    public DataModel() {
//        this.setItem_id(0);
//        this.setName("default name");
//        this.setDescription("default description");
//        this.setQuantity(0);
//        this.setType("default type");
//        this.setAccessCount(0);
//    }

    public DataModel(long item_id, String name, String description, int quantity, String type, int access_count) {
        this.setItem_id(item_id);
        this.setName(name);
        this.setDescription(description);
        this.setQuantity(quantity);
        this.setType(type);
        this.setAccessCount(access_count);
    }

    public int getAccessCount() {
        return access_count;
    }

    public void setAccessCount(int access_count) {
        this.access_count = access_count;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "item_id=" + getItem_id() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", quantity=" + getQuantity() +
                ", type='" + getType() + '\'' +
                ", access_count=" + getAccessCount() +
                '}';
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}