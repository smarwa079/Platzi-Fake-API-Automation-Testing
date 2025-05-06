package DataProviders;

import POJO.Category;
import org.testng.annotations.DataProvider;
import utils.Variables;

public class CategoryDataProvider
{

    @DataProvider (name = "createCategoryValidData")
    public Category[] createCategoryValidData()
    {
        Category validCategory = new Category();

        validCategory.setName(Variables.categoryName);
        validCategory.setImage(Variables.categoryImage);

        return new Category[]
                {
                    validCategory
                };
    }

    @DataProvider (name = "createCategoryInValidImageLink")
    public Category[] createCategoryInValidImageLink()
    {
        Category invalidCategory = new Category();

        invalidCategory.setName(Variables.categoryName);
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

        invalidCategory.setImage(Variables.categoryImage);

        return new Category[]
                {
                        invalidCategory
                };
    }

    @DataProvider (name = "createCategoryWithoutImageLink")
    public Category[] createCategoryWithoutImageLink()
    {
        Category invalidCategory = new Category();

        invalidCategory.setName(Variables.faker.commerce().department());

        return new Category[]
                {
                        invalidCategory
                };
    }

    @DataProvider (name = "updateExistingCategoryValidData")
    public Category[] updateExistingCategoryValidData()
    {
        Category valiCategory = new Category();

        valiCategory.setName(Variables.categoryName + "Updated");
        valiCategory.setImage(Variables.categoryImage + "Updated");

        return new Category[]
                {
                        valiCategory
                };
    }

    @DataProvider (name = "updateExistingCategoryInValidImageLink")
    public Category[] updateExistingCategoryInValidImageLink()
    {
        Category category = new Category();

        category.setName(Variables.categoryName + "Updated");
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

        category.setImage(Variables.categoryImage + "Updated");

        return new Category[]
                {
                        category
                };
    }

    @DataProvider (name = "updateExistingCategoryOnlyName")
    public Category[] updateExistingCategoryOnlyName()
    {
        Category category = new Category();

        category.setName(Variables.categoryName + "Updated");

        return new Category[]
                {
                        category
                };
    }

    @DataProvider (name = "updateNonExistentCategory")
    public Category[] updateNonExistentCategory()
    {
        Category category = new Category();

        category.setName(Variables.faker.commerce().department() + "Updated");
        category.setImage(Variables.faker.internet().image(200, 200, true, category.getName()));

        return new Category[]
                {
                        category
                };
    }

}
