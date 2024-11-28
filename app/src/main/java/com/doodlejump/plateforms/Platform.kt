package com.doodlejump.plateforms

import android.graphics.RectF
import android.util.Log
import com.doodlejump.GameObject
import com.doodlejump.IJumpable
import com.doodlejump.Player
import com.doodlejump.Vector

abstract class Platform(var pos0: Vector, var type : Int): GameObject(size, pos0, type), IJumpable {

    companion object {
        val size = Vector(150F, 40F)
    }

    /*override fun isHit(box: RectF): Boolean {
        return (box.bottom <= pos.y + size.y && box.bottom >= pos.y - box.height() / 3)
                && (pos.x + size.x * 66 / 311 < box.right && box.left < pos.x + size.x * 235 / 311)
    }*/

    override fun whenHit(player: Player) {
        player.rebound()
    }
}