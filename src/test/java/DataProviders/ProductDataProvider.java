package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.Product;
import utils.Variables;

import java.util.ArrayList;

public class ProductDataProvider
{

    @DataProvider (name = "createProductValidData")
    public static Product[] createProductValidData() {

        Product validProduct = new Product();

        validProduct.setTitle(Variables.productTitle);
        validProduct.setPrice(100);
        validProduct.setDescription("Test Product Description");
        validProduct.setCategoryId(Variables.categoryValidId);
        ArrayList<String> images1 = new ArrayList<>();
        images1.add(Variables.productImage);
        validProduct.setImages(images1);

        return new Product[] {
                validProduct
        };
    }

    @DataProvider (name = "createProductMissingCategory")
    public static Product[] createProductMissingCategory() {

        Product inValidProduct = new Product();

        inValidProduct.setTitle(Variables.faker.commerce().productName());
        inValidProduct.setPrice(500);
        inValidProduct.setDescription("A description");
        ArrayList<String> images2 = new ArrayList<>();
        images2.add(Variables.productImage);
        inValidProduct.setImages(images2);

        return new Product[] {
                inValidProduct
        };
    }

    @DataProvider (name = "updateExistingProductValidData")
    public static Product[] updateExistingProductValidData()
    {
        Product updatedProduct = new Product();
        updatedProduct.setTitle(Variables.faker.commerce().productName() + "Updated");
        updatedProduct.setPrice(1000);

        return new Product[]
                {
                        updatedProduct

                };
    }

    @DataProvider (name = "updateNonExistentProductData")
    public static Product[] updateNonExistentProductData()
    {
        Product updatedProduct = new Product();
        updatedProduct.setTitle(Variables.faker.commerce().productName() + "Updated");
        updatedProduct.setPrice(800);

        return new Product[]
                {
                        updatedProduct
                };
    }
}
