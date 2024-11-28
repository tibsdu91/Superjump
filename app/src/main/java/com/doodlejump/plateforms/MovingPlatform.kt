package com.doodlejump.plateforms

import com.doodlejump.*

class MovingPlatform(iPos: Vector): Platform(iPos, R.drawable.movingplateform), IUpdate {

    // Force to a byte because it is the smallest type available
    private var direction : Byte = 1

    companion object {
        const val PLATFORM_SPEED = 10F
    }

    override fun update(game: GameManager) {
        if(pos.x > game.width - size.x) direction = -1
        if(pos.x < 0) direction = 1
        move(Vector( direction * PLATFORM_SPEED, 0F))
    }
}