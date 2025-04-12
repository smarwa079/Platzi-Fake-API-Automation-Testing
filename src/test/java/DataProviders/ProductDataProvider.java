package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.Product;

import java.util.ArrayList;

public class ProductDataProvider
{

    @DataProvider (name = "createProductValidData")
    public static Product[] createProductValidData() {

        Product validProduct = new Product();

        validProduct.setTitle("Test Product");
        validProduct.setPrice(100);
        validProduct.setDescription("Test Product Description");
        validProduct.setCategoryId(2);
        ArrayList<String> images1 = new ArrayList<>();
        images1.add("https://i.ibb.co/9y2392F/test-product-image.jpg");
        validProduct.setImages(images1);

        return new Product[] {
                validProduct
        };
    }

    @DataProvider (name = "createProductInValidData")
    public static Product[] createProductInValidData() {

        Product inValidProduct = new Product();

        inValidProduct.setTitle("T-shirt2s");
        inValidProduct.setPrice(500);
        inValidProduct.setDescription("A description");
        ArrayList<String> images2 = new ArrayList<>();
        images2.add("https://i.ibb.co/9y2392F/test-product-image.jpg");
        inValidProduct.setImages(images2);

        return new Product[] {
                inValidProduct
        };
    }

    @DataProvider (name = "updateExistingProductValidData")
    public static Product[] updateExistingProductValidData()
    {
        Product updatedProduct = new Product();
        updatedProduct.setTitle("Desk");
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
        updatedProduct.setTitle("Dress");
        updatedProduct.setPrice(800);

        return new Product[]
                {
                        updatedProduct
                };
    }
}
