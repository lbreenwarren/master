    import org.junit.jupiter.api.Test;

    import static org.junit.jupiter.api.Assertions.*;

    public class MapTest {

    /*
      The constructor should create a map with zero size
    */
    @Test
    void constructorCreatesZeroSizeMap() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      assertEquals(0, testmap.size());
    }

    /*
      Inserting into an empty map should return true and result in a size of one
    */
    @Test
    void insertWhileEmptyReturnsTrueAndMapSizeIsOne() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      assertTrue(testmap.insert(1, "New"));
      assertEquals(1, testmap.size());
    }

    /*
      using the copy constructor results in twp maps of the same correct size
    */
    @Test
    void copyConstructorMakesCorrectSize() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      Map<String,Integer> mapCopy =  (Map<String,Integer>)testmap.clone();
      assertEquals(testmap.size(), mapCopy.size());
    }

    /*
      Using the copy constructor with an existing map makes its own copy such
      that if the value assiciated with a key is changed in the copy it does
      not change the associated value in the original, and inserts/erases on
      either won't affect the other.
    */
    @Test
    void copyConstructorMakesSeparateCopy()
    {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      Map<Integer, String> mapCopy =  new Map<Integer,String>(testmap);
      mapCopy.insert(4, "test");
      assertNull(testmap.get(4));
      mapCopy.erase(3);
      assertEquals("last", testmap.get(3));
      mapCopy.replace(2, "Replaced");
      assertNotEquals("Replaced", testmap.get(2));
    }
    

    /*
      Inserting a key that already exists should fail and not change the map size
    */
    @Test
    void insertOfSameKeyReturnsFalseAndMapSizeRemainsSame() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      assertFalse(testmap.insert(1, "Test"));
      assertEquals(1, testmap.size());
    }


    /*
      Accessing an existing item using get should return correct value and not
      not change the map size
    */
    @Test
    void getOfExistingKeyReturnsProperValueAndSizeIsSame() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      assertEquals("New", testmap.get(1));
      assertEquals(1, testmap.size());
    }

    /*
      Using get on empty map gives correct result and and insert in an empty
      map should add the item with an appropriate value and increase the map
      size to one
    */
    @Test
    void getThenInsertOnEmptyMapProperlySetsValueAndSizeIsOne() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      assertNull(testmap.get(1));
      testmap.insert(1, "New");
      assertEquals("New", testmap.get(1));
      assertEquals(1, testmap.size());
    }

    /*
      get on missing key produces correct result and insert
      properly sets value and increments size
    */
    @Test
    void getThenInsertOnMissingKeyProperlySetsValueAndIncrementsSize() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      assertEquals("New", testmap.get(1));
      assertEquals(3, testmap.size());
      assertNull(testmap.get(4));
    }

    /*
      Erasing a key that exists i nthe map succeeds and decrements the map size
    */
    @Test
    void eraseOfExistingKeyReturnsTrueAndDecrementsSize() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      assertTrue(testmap.erase(3));
      assertEquals(2, testmap.size());
    }

    /*
      Erase on an empty map fails and keeps the map size at zero
    */
    @Test
    void eraseOnEmptyMapReturnsFalseAndSizeRemainsZero() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      assertFalse(testmap.erase(3));
      assertEquals(0, testmap.size());
    }

    /*
      Erase of a non-existant key fails and does not change the map size
    */
    @Test
    void eraseOfMissingKeyReturnsFalseAndSizeRemainsSame() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      assertFalse(testmap.erase(4));
      assertEquals(3, testmap.size());
    }


    /*
      Using the clone method results in two maps of the same, correct size
    */
    @Test
    @SuppressWarnings("unchecked")
    void cloneMakesCorrectSize() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      Map<Integer, String> mapCopy = (Map<Integer, String>)testmap.clone();
      assertEquals(testmap.size(), mapCopy.size());
      assertEquals(3, testmap.size());
    }

    /*
      Using the clone method with an existing map makes its own copy such that
      if the value associated with a key is changed in the copy it does not
      change the associated value in the original, and inserts/erases on either
      won't affect the other.
    */
    @Test
    @SuppressWarnings("unchecked")
    void cloneMakesSeparateCopy() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      testmap.insert(1, "New");
      testmap.insert(2, "Next");
      testmap.insert(3, "last");
      Map<Integer, String> mapCopy =  (Map<Integer, String>)testmap.clone();
      mapCopy.insert(4, "test");
      assertNull(testmap.get(4));
      mapCopy.erase(3);
      assertEquals("last", testmap.get(3));
      mapCopy.replace(2, "Replaced");
      assertNotEquals("Replaced", testmap.get(2));
    }

    /*
      Using the clone method with an existing map with over 1000 items
      makes a copt that has the correct keys/values at a couple places
      deep within the map (values that aren't at either end of the range
      of keys used
    */
    @Test
    @SuppressWarnings("unchecked")
    void cloneWithManyItemsHasCorrectValues() {
      Map<Integer,String> testmap = new Map<Integer,String>();
      for (int i = 0; i < 1000; ++i) {
        testmap.insert(i, Integer.toString(i));
      }
      Map<Integer, String> mapCopy = (Map<Integer, String>)testmap.clone();
      for (int i = 0; i < 5; ++i) {
        assertEquals(testmap.get(i), mapCopy.get(i));
      }
      for (int i = 510; i < 515; ++i) {
        assertEquals(testmap.get(i), mapCopy.get(i));
      }
      for (int i = 950; i < 955; ++i) {
        assertEquals(testmap.get(i), mapCopy.get(i));
      }
    }
  }
