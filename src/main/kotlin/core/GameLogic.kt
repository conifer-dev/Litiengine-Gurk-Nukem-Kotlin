package core

import de.gurkenlabs.litiengine.Direction
import de.gurkenlabs.litiengine.Game
import de.gurkenlabs.litiengine.abilities.Ability
import de.gurkenlabs.litiengine.abilities.AbilityInfo
import de.gurkenlabs.litiengine.abilities.effects.EffectApplication
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget
import de.gurkenlabs.litiengine.abilities.effects.ForceEffect
import de.gurkenlabs.litiengine.entities.CollisionBox
import de.gurkenlabs.litiengine.entities.Creature
import de.gurkenlabs.litiengine.entities.EntityPivotType
import de.gurkenlabs.litiengine.entities.IMobileEntity
import de.gurkenlabs.litiengine.environment.Environment
import de.gurkenlabs.litiengine.graphics.Camera
import de.gurkenlabs.litiengine.graphics.PositionLockCamera
import de.gurkenlabs.litiengine.physics.Force
import de.gurkenlabs.litiengine.physics.GravityForce
import player.Player


object GameLogic {

    // Initialises the game logic

    fun init() {

        // Creating camera that is locked on the player
        val camera: Camera = PositionLockCamera(Player)
        camera.isClampToMap = true
        Game.world().setCamera(camera)

        // Set a basic gravity for all levels
        Game.world().setGravity(120)

        // add default game logic for when a level was loaded
        Game.world().onLoaded { e: Environment ->
            // spawn the player instance on the spawn point with the name "enter"
            val enter = e.getSpawnpoint("enter").apply {
                spawn(Player)
            }
        }
    }
}

@AbilityInfo(cooldown = 500, origin = EntityPivotType.COLLISIONBOX_CENTER, duration = 300, value = 240)
class Jump(executor: Creature) : Ability(executor) {
    init {
        addEffect(JumpEffect(this))
    }

    private inner class JumpEffect(ability: Ability) :
        ForceEffect(ability, ability.attributes.value().get().toInt().toFloat(), EffectTarget.EXECUTINGENTITY) {
        override fun applyForce(affectedEntity: IMobileEntity): Force {
            // create a new force and apply it to the player
            val force = GravityForce(affectedEntity, strength, Direction.UP)
            affectedEntity.movement().apply(force)
            return force
        }

        override fun hasEnded(appliance: EffectApplication): Boolean {
            return super.hasEnded(appliance) || isTouchingCeiling
        }

        /**
         * Make sure that the jump is cancelled when the entity touches a static collision box above it.
         *
         * @return True if the entity touches a static collision box above it.
         */
        private val isTouchingCeiling: Boolean
            get() {
                val opt = Game.world().environment().collisionBoxes.stream().filter { x: CollisionBox ->
                    x.boundingBox.intersects(ability.executor.boundingBox)
                }.findFirst()
                if (!opt.isPresent) {
                    return false
                }
                val box = opt.get()
                return box.collisionBox.maxY <= ability.executor.collisionBox.minY
            }
    }
}