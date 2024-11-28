package com.doodlejump.plateforms

import com.doodlejump.*
import kotlin.math.pow
import kotlin.math.sqrt

class DurationPlatform(iPos: Vector): Platform(iPos, R.drawable.durationplateform), IUpdate {

    private var obs = TimeObservable(100, this);

    override fun update(game: GameManager) {
        obs.update()
        paint.alpha = (255F * (obs.duration.toFloat() / obs.maxDuration.toFloat()).pow(1F/4F)).toInt()
        // Fonction parce que l'oeuil humain ne voit pas très bien les alpha faible, ici la racine donne l'avantage d'avoir un alpha qui décroit relativement vers la fin
    }

    override fun whenHit(player: Player) {
        super.whenHit(player)
        obs.start()
    }

}