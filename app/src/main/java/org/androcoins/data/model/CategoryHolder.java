package org.androcoins.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds list of categories for concrete type
 * @author vitaly gashock
 */
public class CategoryHolder {
    public int title;
    public List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    public CategoryHolder(int title, List<CategoryEntity> categories) {
        this.title = title;
        this.categories = categories;
    }
}
