package DataProviders;

import POJO.Category;
import org.testng.annotations.DataProvider;

public class CategoryDataProvider
{

    @DataProvider (name = "createCategoryValidData")
    public Category[] createCategoryValidData()
    {
        Category validCategory = new Category();

        validCategory.setName("Fruits");
        validCategory.setImage("https://imgur.com/SvL0zaZ");

        return new Category[]
                {
                    validCategory
                };
    }

    @DataProvider (name = "createCategoryInValidImageLink")
    public Category[] createCategoryInValidImageLink()
    {
        Category invalidCategory = new Category();

        invalidCategory.setName("Books");
        invalidCategory.setImage("link");

        return new Category[]
                {
                        invalidCategory
                };
    }

    @DataProvider (name = "createCategoryWithoutName")
    public Category[] createCategoryWithoutName()
    {
        Category invalidCategory = new Category();

        invalidCategory.setImage("https://imgur.com/SvL0zaZ");

        return new Category[]
                {
                        invalidCategory
                };
    }

    @DataProvider (name = "createCategoryWithoutImageLink")
    public Category[] createCategoryWithoutImageLink()
    {
        Category invalidCategory = new Category();

        invalidCategory.setName("Headphones");

        return new Category[]
                {
                        invalidCategory
                };
    }

    @DataProvider (name = "updateExistingCategoryValidData")
    public Category[] updateExistingCategoryValidData()
    {
        Category valiCategory = new Category();

        valiCategory.setName("suits");
        valiCategory.setImage("https://imgur.com/a/Jy9Vb2Z");

        return new Category[]
                {
                        valiCategory
                };
    }

    @DataProvider (name = "updateExistingCategoryInValidImageLink")
    public Category[] updateExistingCategoryInValidImageLink()
    {
        Category category = new Category();

        category.setName("Toys");
        category.setImage("link");

        return new Category[]
                {
                        category
                };
    }

    @DataProvider (name = "updateExistingCategoryOnlyImageLink")
    public Category[] updateExistingCategoryOnlImageLink()
    {
        Category category = new Category();

        category.setImage("https://imgur.com/HmvYLNi");

        return new Category[]
                {
                        category
                };
    }

    @DataProvider (name = "updateExistingCategoryOnlyName")
    public Category[] updateExistingCategoryOnlyName()
    {
        Category category = new Category();

        category.setName("Garden tools");

        return new Category[]
                {
                        category
                };
    }

    @DataProvider (name = "updateNonExistentCategory")
    public Category[] updateNonExistentCategory()
    {
        Category category = new Category();

        category.setName("Sewing Kit");
        category.setImage("https://imgur.com/1cI820n");

        return new Category[]
                {
                        category
                };
    }

}
