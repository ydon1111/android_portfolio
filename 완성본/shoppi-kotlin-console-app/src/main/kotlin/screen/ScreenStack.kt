package screen

object ScreenStack {
    private val screenStack = mutableListOf<Screen>()

    fun push(screen: Screen){
        screenStack.add(screen)
    }

    fun pop(){
        screenStack.removeLastOrNull()
    }

    fun peak(): Screen?{
        return screenStack.lastOrNull()
    }
}

sealed class Screen