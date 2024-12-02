package com.doodlejump.plateforms

import com.doodlejump.Player
import com.doodlejump.R
import com.doodlejump.Vector

class FalsePlatform(iPos: Vector): Platform(iPos, R.drawable.wooden_platform_with_a_visible_crack_removebg) {

    override fun whenHit(player: Player) {
        // Self destroy, no budget for the animation xD
        removed = true
    }
}