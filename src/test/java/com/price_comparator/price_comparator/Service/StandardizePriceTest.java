package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.DTO.FinalPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StandardizePriceTest extends AbstractBaseTest {

    @Autowired
    BestPricePerUnitService bestPricePerUnitService;

    @Test
    void testConvertMlToL(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "lapte",
                "zuzu",
                200.0,
                "ml",
                "lactate",
                "lidl",
                5.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "lapte",
                "zuzu",
                200.0,
                "ml",
                "lactate",
                "lidl",
                5.0,
                25.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertGtoKg(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                100.0,
                "g",
                "seminte",
                "lidl",
                2.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                100.0,
                "g",
                "seminte",
                "lidl",
                2.0,
                20.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertKgToKg(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                0.5,
                "kg",
                "seminte",
                "lidl",
                10.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                0.5,
                "kg",
                "seminte",
                "lidl",
                10.0,
                20.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertMultipleKgToKg(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                2.0,
                "kg",
                "seminte",
                "lidl",
                10.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "seminte",
                "nutline",
                2.0,
                "kg",
                "seminte",
                "lidl",
                10.0,
                5.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertMultipleLToL(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "lapte",
                "zuzu",
                2.0,
                "l",
                "lactate",
                "lidl",
                15.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "lapte",
                "zuzu",
                2.0,
                "l",
                "lactate",
                "lidl",
                15.0,
                7.5
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertMultipleBucToBuc(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "oua",
                "lidl",
                6.0,
                "buc",
                "oua",
                "lidl",
                30.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "oua",
                "lidl",
                6.0,
                "buc",
                "oua",
                "lidl",
                30.0,
                5.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

    @Test
    void testConvertMultipleRoleToRole(){

        FinalPrice initialPrice = new FinalPrice(
                "P001",
                "hartie",
                "lidl",
                8.0,
                "role",
                "ingrijire",
                "lidl",
                40.0,
                -1.0
        );

        FinalPrice expectedResult = new FinalPrice(
                "P001",
                "hartie",
                "lidl",
                8.0,
                "role",
                "ingrijire",
                "lidl",
                40.0,
                5.0
        );

        FinalPrice finalPrice = bestPricePerUnitService.standardizePrice(initialPrice);
        assert (finalPrice.equals(expectedResult));
    }

}
