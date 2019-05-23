package com.udacity.sandwichclub.model;

import java.util.List;

/**
 * The class describes a sandwich over the attributes sandwich name, also known as the sandwich name,
 * place of origin, description of the sandwich, image of the sandwich and ingredients of the sandwich.
 */
public class Sandwich {
    private static final String NO_INFORMATION_AVAILABLE = "no information available";
    private static final String COMMA_SEPARATED_LIST = ", ";

    private String mainName;
    private String alsoKnownAs;
    private String placeOfOrigin;
    private String description;
    private String image;
    private String ingredients;

    /**
     * constructor
     *
     * @param mainName name of the sandwich
     * @param alsoKnownAs also known as the sandwich name
     * @param placeOfOrigin place of origin
     * @param description of the sandwich
     * @param image of the sandwich
     * @param ingredients of the sandwich
     */
    public Sandwich(String mainName, List<String> alsoKnownAs, String placeOfOrigin, String description, String image, List<String> ingredients) {
        this.mainName = mainName;
        this.alsoKnownAs = (alsoKnownAs == null || alsoKnownAs.isEmpty()) ? NO_INFORMATION_AVAILABLE : String.join(COMMA_SEPARATED_LIST, alsoKnownAs);
        this.placeOfOrigin = placeOfOrigin;
        this.description = description;
        this.image = image;
        this.ingredients =  (ingredients == null || ingredients.isEmpty()) ? NO_INFORMATION_AVAILABLE : String.join(COMMA_SEPARATED_LIST, ingredients);


    }

    public String getMainName() {
        return mainName;
    }



    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }



    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }



    public String getDescription() {
        return description;
    }



    public String getImage() {
        return image;
    }



    public String getIngredients() {
        return ingredients;
    }


}
