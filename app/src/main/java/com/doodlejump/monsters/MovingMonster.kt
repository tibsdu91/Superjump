package com.doodlejump.monsters

import com.doodlejump.GameManager
import com.doodlejump.IUpdate
import com.doodlejump.R
import com.doodlejump.Vector


class MovingMonster(iPos: Vector): Monster(iPos, Vector(187f, 121f), R.drawable.movingmonster), IUpdate {


    private var direction : Byte = 1

    companion object {
        const val MONSTER_SPEED = 10F
    }

    override fun update(game: GameManager) {
        if(pos.x > game.width - size.x) direction = -1
        if(pos.x < 0) direction = 1
        move(Vector( direction * MONSTER_SPEED, 0F))
    }
}