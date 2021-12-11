package player

import core.Jump
import de.gurkenlabs.litiengine.Game
import de.gurkenlabs.litiengine.IUpdateable
import de.gurkenlabs.litiengine.entities.*
import de.gurkenlabs.litiengine.input.PlatformingMovementController
import de.gurkenlabs.litiengine.physics.Collision
import de.gurkenlabs.litiengine.physics.IMovementController
import java.awt.geom.Rectangle2D


@EntityInfo(width = 18f, height = 18f)
@MovementInfo(velocity = 70f)
@CollisionInfo(collisionBoxWidth = 8f, collisionBoxHeight = 16f, collision = true)


object Player : Creature("gurknukem"), IUpdateable {
    private const val MAX_ADDITIONAL_JUMPS = 1
    private var jump = Jump(this)
    private var consecutiveJumps: Int = 0

    override fun update() {
        if (isTouchingGround()) {
            consecutiveJumps = 0
        }
    }

    override fun createMovementController(): IMovementController {
        return PlatformingMovementController(this)
    }

    /**
     * Checks whether this instance can currently jump and then performs the core.Jump ability.
     *
     * Note that the name of this methods needs to be equal to {@link PlatformingMovementController#JUMP}} in order for the controller
     * to be able to use this method.
     * Another option is to explicitly specify the Action.name() attribute on the annotation.
     */

    @Action(description = "This performs the jump ability for the player's entity.")
    fun jump() {
        if (consecutiveJumps >= MAX_ADDITIONAL_JUMPS || !jump.canCast()) {
            return
        }

        jump.cast()
        consecutiveJumps++
    }


    private fun isTouchingGround(): Boolean {

        // the idea of this ground check is to extend the current collision box by
        // one pixel and see if
        // a) it collides with any static collision box

        val groundCheck: Rectangle2D = Rectangle2D.Double(
            collisionBox.x, collisionBox.y, collisionBoxWidth, collisionBoxHeight + 1
        )

        // b) it collides with the map's boundaries

        return if (groundCheck.maxY > Game.physics().bounds.maxY) {
            true
        } else Game.physics().collides(groundCheck, Collision.STATIC)
    }
}
