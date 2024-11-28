package com.doodlejump.boosts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.util.Log
import com.doodlejump.*

class Jetpack(iPos: Vector): GameObject(size, iPos, R.drawable.jetpack), IUpdate {

    private var obs = TimeObservable(100, this)

    companion object {
        val size = Vector(80F, 108F)
    }

    override fun whenHit(player: Player) {
        hitbox = RectF(0F, 0F, 0F, 0F)
        pos = Vector(-1000F, 1000F)
        obs.start()
    }

    override fun update(game: GameManager) {
        obs.update()
        if(obs.started) {
            pos.y = 1000F
            game.player.speed.y = 100F
            if(obs.duration == obs.maxDuration - 1) game.player.changeJetpack(true, game)
            if(obs.duration == 0) game.player.changeJetpack(false, game)
        }
    }
}