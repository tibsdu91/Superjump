package com.doodlejump.plateforms

import com.doodlejump.Player
import com.doodlejump.R
import com.doodlejump.Vector

class OneUsePlatform(iPos: Vector): Platform(iPos, R.drawable.oneuseplateform) {

    override fun whenHit(player: Player) {
        if(player.speed.y < 0) removed = true
        player.rebound()
    }
}