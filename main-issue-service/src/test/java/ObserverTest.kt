import org.junit.jupiter.api.Test
import java.util.Observable
import java.util.Observer

class ObserverTest {

    @Test
    fun `observer test`() {
        val barista = Barista()

        barista.orderCoffee("아이스 아메리카노")

        val customer = Customer("고객 1")
        val customer2 = Customer("고객 2")
        val customer3 = Customer("고객 3")

        barista.addObserver(customer)
        barista.addObserver(customer2)
        barista.addObserver(customer3)

        barista.makeCoffee()
    }
}

class Coffee(val name: String)

class Barista : Observable() {

    private lateinit var coffeeName: String
    fun orderCoffee(name: String) {
        this.coffeeName = name
    }

    fun makeCoffee() {
        setChanged()
        notifyObservers(Coffee(this.coffeeName))
    }
}

class Customer(val name: String) : Observer {

    override fun update(o: Observable?, arg: Any?) {
        val coffee = arg as Coffee
        println("${name}이 ${coffee.name}을 받았습니다")
    }


}