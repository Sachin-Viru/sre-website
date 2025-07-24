package com.example.srewebsite.model;

public class Tool {
    private String name;
    private String logo;  // Now contains SVG markup
    private String description;

    public Tool(String name, String logo, String description) {
        this.name = name;
        this.logo = logo;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getDescription() {
        return description;
    }
}
