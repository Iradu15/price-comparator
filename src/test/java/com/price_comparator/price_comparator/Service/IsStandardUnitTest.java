package com.price_comparator.price_comparator.Service;


import com.price_comparator.price_comparator.AbstractBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IsStandardUnitTest extends AbstractBaseTest {
    @Test
    void testIsStandardUnitL(){
        assert(BestPricePerUnitService.isStandardUnit("l"));
    }

    @Test
    void testIsStandardUnitKg(){
        assert(BestPricePerUnitService.isStandardUnit("kg"));
    }

    @Test
    void testIsStandardUnitBuc(){
        assert(BestPricePerUnitService.isStandardUnit("buc"));
    }

    @Test
    void testIsStandardUnitRole(){
        assert(BestPricePerUnitService.isStandardUnit("role"));
    }

    @Test
    void testIsNotStandardUnitG(){
        assert(!BestPricePerUnitService.isStandardUnit("g"));
    }

    @Test
    void testIsNotStandardUnitMl(){
        assert(!BestPricePerUnitService.isStandardUnit("ml"));
    }

    @Test
    void testIsNotStandardUnitUnknown(){
        assert(!BestPricePerUnitService.isStandardUnit("m"));
    }
}
