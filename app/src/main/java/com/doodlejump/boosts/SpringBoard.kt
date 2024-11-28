package com.doodlejump.boosts

import com.doodlejump.*

class SpringBoard(iPos: Vector): GameObject(size, iPos, R.drawable.springboard), IJumpable {

    companion object {
        val size = Vector(140F, 50F)
    }

    override fun whenHit(player: Player) {
        if(player.speed.y < 0F) player.speed.y += 300F
    }
}