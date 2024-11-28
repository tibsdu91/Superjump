package com.doodlejump.monsters

import android.graphics.BitmapFactory
import android.graphics.Paint
import com.doodlejump.*
import com.doodlejump.Player

open class Monster(pos0: Vector, iSize: Vector, type: Int): GameObject(iSize, pos0, type){

    constructor(pos0: Vector) : this(pos0, Vector(217F, 144F), R.drawable.monster)

    companion object {
        val size = Vector(217F, 144F)
    }

    override fun whenHit(player: Player) {
        if(player.speed.y < 0 ) {
            removed = true
            player.rebound()
        } else {
            player.die()
            player.speed.y = -50F
        }
    }





}
