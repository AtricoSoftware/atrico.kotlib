import atrico.kotlib.Hash
import atrico.kotlib.testing.TestBase
import atrico.kotlib.testing.notEqualTo
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestClass : TestBase() {
    @Test
    fun testNoItems() {
        // Act & Assert
        getHash()
    }

    @Test
    fun testItems() {
        // Act & Assert
        getHash("a", random.nextInt(), random.nextDouble(), 'o')
    }

    @Test
    fun testNullItems() {
        // Act & Assert
        getHash("b", random.nextInt(), null, 'z')
    }

    @Test
    fun testEqualItems() {
        // Arrange
        val obj1 = object {
            override fun hashCode() = 123
        }
        val obj2 = object {
            override fun hashCode() = 123
        }

        // Act
        val hash1 = getHash(obj1)
        val hash2 = getHash(obj2)

        // Assert
        assertThat("equal", hash1, equalTo(hash2))
    }

    @Test
    fun testDifferentOrder() {
        // Arrange
        // Act
        val hash1 = getHash(1,2,3,4)
        val hash2 = getHash(2,1,3,4)

        // Assert
        assertThat("not equal", hash1, notEqualTo(hash2))
    }

    // no items
    private fun getHash(): Int {
        // Get hashes from vararg versions
        val resultObjVar = Hash().value
        val resultCalcVar = Hash.calculate()
        val resultObjAddVar = Hash().add().value
        println("Obj(var): $resultObjVar")
        println("Calculate(var): $resultCalcVar")
        println("Obj Add (var): $resultObjAddVar")

        // Continue with list versions
        return getHashList(emptyList(), resultObjVar, resultCalcVar, resultObjAddVar)
    }

    // 1 item
    private fun getHash(item1: Any?): Int {
        // Get hashes from vararg versions
        val resultObjVar = Hash(item1).value
        val resultCalcVar = Hash.calculate(item1)
        val resultObjAddVar = Hash().add(item1).value
        println("Obj(var): $resultObjVar")
        println("Calculate(var): $resultCalcVar")
        println("Obj Add (var): $resultObjAddVar")

        // Continue with list versions
        return getHashList(listOf(item1), resultObjVar, resultCalcVar, resultObjAddVar)
    }

    // 4 items
    private fun getHash(item1: Any?, item2: Any?, item3: Any?, item4: Any?): Int {
        // Get hashes from vararg versions
        val resultObjVar = Hash(item1, item2, item3, item4).value
        val resultCalcVar = Hash.calculate(item1, item2, item3, item4)
        val resultObjAddVar = Hash().add(item1, item2, item3, item4).value
        println("Obj(var): $resultObjVar")
        println("Calculate(var): $resultCalcVar")
        println("Obj Add (var): $resultObjAddVar")

        // Continue with list versions
        return getHashList(listOf(item1, item2, item3, item4), resultObjVar, resultCalcVar, resultObjAddVar)
    }

    private fun getHashList(list: Iterable<Any?>, resultObjVar: Int, resultCalcVar: Int, resultObjAddVar: Int): Int {
        // Get hashes from list versions
        val resultObjList = Hash(list).value
        val resultCalcList = Hash.calculate(list)
        val resultObjAddList = Hash().add(list).value
        println("Obj: $resultObjList")
        println("Calculate: $resultCalcList")
        println("Obj Add: $resultObjAddList")
        println()

        // Assert all versions are equal
        assertThat("hashes equal (list)", resultObjList, equalTo(resultCalcList))
        assertThat("hashes equal (obj var)", resultObjList, equalTo(resultObjVar))
        assertThat("hashes equal (calc var)", resultObjList, equalTo(resultCalcVar))
        assertThat("hashes equal (obj add)", resultObjList, equalTo(resultObjAddList))
        assertThat("hashes equal (obj add var)", resultObjList, equalTo(resultObjAddVar))
        // Return the value
        return resultObjList
    }
}