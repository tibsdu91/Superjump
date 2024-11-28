package com.doodlejump.boosts

import com.doodlejump.*

class Spring(iPos: Vector): GameObject(size, iPos, R.drawable.spring), IJumpable {

    companion object {
        val size = Vector(55F, 55F)
    }

    override fun whenHit(player: Player) {
        if(player.speed.y < 0F) player.speed.y += 200F
    }
}