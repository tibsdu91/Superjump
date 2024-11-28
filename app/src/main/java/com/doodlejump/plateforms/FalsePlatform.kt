package com.doodlejump.plateforms

import com.doodlejump.Player
import com.doodlejump.R
import com.doodlejump.Vector

class FalsePlatform(iPos: Vector): Platform(iPos, R.drawable.falseplateform) {

    override fun whenHit(player: Player) {
        // Self destroy, no budget for the animation xD
        removed = true
    }
}