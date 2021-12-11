package player

import de.gurkenlabs.litiengine.input.Input
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

object PlayerInput {
    fun init() {
        // make the game exit upon pressing ESCAPE (by default there is no such key binding and the window needs to be shutdown otherwise, e.g. ALT-F4 on Windows)
        Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE) { exitProcess(0) }
    }
}