package bg.sofia.uni.fmi.mjt.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bg.sofia.uni.fmi.mjt.api.FoodDataApiClientTest;
import bg.sofia.uni.fmi.mjt.cache.ServerCacheTest;
import bg.sofia.uni.fmi.mjt.cache.storage.BrandedFoodStorageTest;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodDetailsStorageTest;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodStorageTest;
import bg.sofia.uni.fmi.mjt.commands.GetFoodByBarcodeTest;
import bg.sofia.uni.fmi.mjt.commands.GetFoodByNameTest;
import bg.sofia.uni.fmi.mjt.commands.GetFoodReportByIdTest;
import bg.sofia.uni.fmi.mjt.parser.ClientMessageParserTest;

@RunWith(Suite.class)
@SuiteClasses({
    FoodDataApiClientTest.class,
    ServerCacheTest.class,
    BrandedFoodStorageTest.class,
    FoodDetailsStorageTest.class,
    FoodStorageTest.class,
    GetFoodByBarcodeTest.class,
    GetFoodByNameTest.class,
    GetFoodReportByIdTest.class,
    ClientMessageParserTest.class
})
public class AllTests {

}
