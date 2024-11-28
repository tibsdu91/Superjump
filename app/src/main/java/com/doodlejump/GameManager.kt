package com.doodlejump

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.doodlejump.boosts.Jetpack
import com.doodlejump.boosts.Spring
import com.doodlejump.boosts.SpringBoard
import com.doodlejump.plateforms.*
import kotlin.math.floor
import kotlin.random.Random
import com.doodlejump.monsters.Monster
import java.lang.Thread.sleep
import com.doodlejump.monsters.*


class GameManager @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), Runnable  {

    private var objects = arrayListOf<GameObject>()
    private var addStack = arrayListOf<GameObject>()
    private var drawing = true;
    private var totalElapsedTime = 0.0
    private var backgroundPaint = Paint()
    private var scorePaint = Paint()
    private var genStep = 3 * Platform.size.y
    private var genBuffer = 0F
    private lateinit var thread: Thread

    var score = 0F
    var player = Player(Vector(400F, 10F))
    lateinit var canvas: Canvas
    lateinit var activity: MainActivity

    companion object {
        const val TIME_CONSTANT = 0.3F
        const val MS_PER_TICK = 25.0
        const val DENSITY = 0.7F
        const val SCORE_MULTIPLIER = 0.1F
        const val WIDTH = 1074f
        const val HEIGHT = 1584f
    }

    init {
        scorePaint.color = Color.BLACK
        scorePaint.textSize = 100F
        for (i in 1..(HEIGHT / genStep).toInt()) generatePlatform(Random.nextFloat() * (WIDTH - Platform.size.x), genStep * i)
        addStack.removeAll{it is Monster}
        backgroundPaint.color = Color.WHITE
    }

    private fun gameLoop() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawColor( 0, PorterDuff.Mode.CLEAR );
            canvas.drawText("${score.toInt()}", 100F, 100F, scorePaint)

            objects.forEach {
                if(it is IUpdate) it.update(this)
                it.draw(this)
            }
            objects.removeAll{ it.removed }
            player.update(this)
            player.draw(this)
            player.checkCollisions(objects)

            objects.addAll(addStack); addStack.clear()
            holder.unlockCanvasAndPost(canvas)
        }
        if(!player.alive) activity.endScreen(score.toInt())
    }

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            var elapsedTimeMS:Double=(currentTime-previousFrameTime).toDouble()
            gameLoop()
            if (elapsedTimeMS < MS_PER_TICK) sleep((MS_PER_TICK - elapsedTimeMS).toLong())
            totalElapsedTime += elapsedTimeMS / 1000.0
            previousFrameTime = currentTime
        }
    }

    fun onPause() {
        drawing = false
        thread.join()
    }

    fun onResume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    fun setXOrientation(deg: Float) {
        if(player.alive) player.speed.x = deg / 10
    }

    private fun changeScore(amount: Float) {
        if(player.alive) score += amount * SCORE_MULTIPLIER
    }

    private fun generatePlatform(x: Float, y: Float) {
        var r = Random.nextFloat() * 100
        if(0 < r && r < 5f) addStack.add(MovingPlatform(Vector(x, y)))
        else if(5 < r && r < 10) addStack.add(OneUsePlatform(Vector(x, y)))
        else if(10 < r && r < 15) addStack.add(DurationPlatform(Vector(x, y)))
        else if(20 < r && r < 25) addStack.add(MovingMonster(Vector(x, y)))
        else {
            addStack.add(BasePlatform(Vector(x, y)))
            if(25 < r && r < 30) addStack.add(Spring(Vector(x, y + Spring.size.y/2)))
            if(30 < r && r < 35) addStack.add(SpringBoard(Vector(x + Platform.size.x / 2 - SpringBoard.size.x / 2, y + SpringBoard.size.y/2)))
            if(15 < r && r < 20) addStack.add(Monster(Vector(x , y + Monster.size.y * 5 / 6)))
            if(35 < r && r < 40) addStack.add(Jetpack(Vector(x + Platform.size.x / 2 - Jetpack.size.x / 2, y + Jetpack.size.y/2)))
        }
    }

    fun moveObjects(amount: Float) {
        // Generation of the new plateforms
        genBuffer += amount
        for (i in 1..floor(genBuffer * DENSITY / (genStep)).toInt()) {
            generatePlatform(Random.nextFloat() * (WIDTH - Platform.size.x), genBuffer / i + HEIGHT)
            genBuffer -= genStep
        }

        // Moving the player up
        changeScore(amount)
        objects.forEach {
            it.move(Vector(0F, -amount))
            it.checkRemove()
        }
    }

}