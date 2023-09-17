import org.junit.jupiter.api.Test

class IteratorTest {

    @Test
    fun `iterable`() {
        val carIterable = CarIterable(listOf(Car("rambo"), Car("pera")))

        val iterator = carIterable.iterator()

        while(iterator.hasNext()) {
            val next = iterator.next()

            println(next.brand)
        }
    }
}

data class Car(val brand: String)

class CarIterable(val cars: List<Car> = listOf()) : Iterable<Car> {

    override fun iterator(): Iterator<Car> = CarIterator(cars)

}

class CarIterator(val cars: List<Car> = listOf(), var index : Int = 0) : Iterator<Car> {
    override fun hasNext(): Boolean {
        return cars.size > index
    }

    override fun next(): Car {
        return cars[index++]
    }

}