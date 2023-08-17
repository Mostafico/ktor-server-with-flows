import io.ktor.serialization.gson.gson
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.MutableStateFlow

class MyServer() {
    val flow = MutableStateFlow<ItemState>(ItemState.Initial)

    val server by lazy {
        embeddedServer(Netty, port = 5000) {

            install(ContentNegotiation) {
                gson()
            }
            routing {
                post("/order") {
                    val received = call.receive<Item>()
                    flow.emit(ItemState.ItemReceived(received))
                    println("Received on Server $received")
                }
                get("/") {
                    call.respondText("Hello World")
                }
            }
        }
    }
}

sealed class ItemState {
    object Initial : ItemState()
    data class ItemReceived(val item: Item) : ItemState()
}

data class Item(val id: Int, val name: String)
