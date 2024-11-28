package com.doodlejump

import android.graphics.*

abstract class GameObject(protected var size: Vector, protected var pos: Vector, protected val sprite: Int) {

    var removed = false

    protected var hitbox = RectF(0F, 0F, 0F, 0F)
    protected var paint = Paint()
    protected var ressource: Bitmap? = null

    var hitboxPaint = Paint()

    init {
        move(Vector(0f, 0f))
        hitboxPaint.color = Color.GREEN
        hitboxPaint.alpha = 100
    }
    abstract fun whenHit(player: Player)
    open fun isHit(box: RectF): Boolean { return box.intersect(hitbox) }

    open fun draw(game: GameManager) {
        var wd = game.width / GameManager.WIDTH
        var hd = game.height / GameManager.HEIGHT
        if(ressource == null) ressource = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.context.resources, sprite), (size.x * wd).toInt(), (size.y * hd).toInt(), false)
        ressource?.let { game.canvas.drawBitmap(it, pos.x * wd, (GameManager.HEIGHT - pos.y) * hd, paint) }
    }

    fun checkRemove() {
        if(pos.y < 0) removed = true
    }

    fun move(inc: Vector) {
        this.pos += inc
        hitbox.left = pos.x
        hitbox.top = pos.y - size.y
        hitbox.right = pos.x + size.x
        hitbox.bottom = pos.y
    }
}