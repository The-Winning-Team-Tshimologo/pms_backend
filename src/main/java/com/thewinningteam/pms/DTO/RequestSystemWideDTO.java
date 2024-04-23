package com.thewinningteam.pms.DTO;


import com.thewinningteam.pms.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class RequestSystemWideDTO {
    private byte[] pictures;
    private String streetName;
    private String city;
    private int rating;
    private String firstName;
    private String lastName;
    private String category;

    // Constructor matching the query arguments
    public RequestSystemWideDTO(byte[] pictures, String streetName, String city, int rating, String firstName, String lastName, String category) {
        this.pictures = pictures;
        this.streetName = streetName;
        this.city = city;
        this.rating = rating;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
    }
}

