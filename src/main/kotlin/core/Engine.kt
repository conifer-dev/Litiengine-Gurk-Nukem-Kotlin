package core

import de.gurkenlabs.litiengine.Game
import de.gurkenlabs.litiengine.resources.Resources
import player.PlayerInput

import screens.InGameScreen

class Engine {
    fun run() {

        // Setting game information
        Game.info().let {
            it.name = "KOTLIN TEST"
            it.subTitle = ""
            it.version = "v0.0.1"
            it.description = "Example 2D Platformer made with Litiengine & Kotlin"
        }

        // Init game infrastructure
        Game.init()

        // Setting icon for the game
        Game.window().setIcon(Resources.images().get("icon.png"))
        Game.graphics().baseRenderScale = 4f

        // Load data from the utiLITI game file
        Resources.load("game.litidata")

        PlayerInput.init()
        GameLogic.init()

        Game.screens().add(InGameScreen)

        // Load the first level
        Game.world().loadEnvironment("level1")
        Game.start()
    }
}