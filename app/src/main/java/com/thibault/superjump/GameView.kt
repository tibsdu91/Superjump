package com.thibault.superjump

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat

class GameView(context: Context) : SurfaceView(context), SensorEventListener {
    private val TAG: String = "DoodleJump"

    // Game components
    private var gameLoopThread: GameLoopThread? = null
    private val surfaceHolder = holder
    private var doodler = Doodler()
    private var platformManager = PlatformManager()
    private var playButton = PlayButton()

    private var score = 0
    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private var gameStarted = false
    private var isGameOver: Boolean = false

    private val NUM_PLATFORMS = 16
    private var PLATFORM_GAP: Int = 0

    // Sensor-related variables
    private var sensorManager: SensorManager
    private var accelerometer: Sensor?

    // Sounds
    private val sounds = HashMap<String, MediaPlayer>()
    val soundIds = arrayOf(R.raw.jump, R.raw.gameover, R.raw.spring, R.raw.platform_break)
    val soundNames = arrayOf("jump", "gameover", "spring", "platform_break")

    // Graphics
    private var spritesImage: Bitmap
    private var gridImage: Bitmap
    private var sketchLine: Bitmap
    private var textPaint: Paint
    private var scorePaint: Paint
    private var gameOverScorePaint: Paint
    private var scoreHeader: Paint
    private var maxHeight = 0

    private var density: Float = 1f
    private var platformWidth: Int = 0
    private var platformHeight: Int = 30

    init {
        // Register sensor listener
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        } else {
            Log.e(TAG, "Accelerometer not available on this device")
        }

        // Load sounds
        for (i in soundIds.indices) {
            val mediaPlayer = MediaPlayer.create(context, soundIds[i])
            sounds[soundNames[i]] = mediaPlayer
        }

        // Screen dimensions
        val displayMetrics = Resources.getSystem().displayMetrics
        density = displayMetrics.density
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        // Load images
        spritesImage = BitmapFactory.decodeResource(context.resources, R.drawable.doodlejump)
        gridImage = BitmapFactory.decodeResource(context.resources, R.drawable.grid)
        sketchLine = BitmapFactory.decodeResource(context.resources, R.drawable.sketch_line)

        platformHeight = (platformHeight * density).toInt()
        platformWidth = spritesImage.width

        // Initialize paints
        textPaint = Paint().apply {
            textSize = 160f
            color = Color.parseColor("#5a5816")
            typeface = ResourcesCompat.getFont(context, R.font.gh_regular)
        }
        scorePaint = Paint().apply {
            textSize = 60f
            color = Color.BLACK
            typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.gh_regular), Typeface.BOLD)
        }
        gameOverScorePaint = Paint().apply {
            textSize = 60f
            color = Color.parseColor("#5f97bf")
            typeface = ResourcesCompat.getFont(context, R.font.gh_regular)
        }
        scoreHeader = Paint().apply {
            color = Color.parseColor("#177dad")
            alpha = 220
        }

        PLATFORM_GAP = screenHeight / NUM_PLATFORMS

        // Initialize game objects
        doodler.x = (screenWidth / 2f) - doodler.getCenterOffset()
        doodler.y = playButton.y - (doodler.height * doodler.scaleFactor)

        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                startGameThread()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                stopGame()
            }
        })
    }

    fun resume() {
        if (gameLoopThread?.isAlive == false) {
            gameLoopThread = GameLoopThread(this)
            gameLoopThread?.running = true
            gameLoopThread?.start()
        } else {
            gameLoopThread?.running = true
        }
    }

    fun pause() {
        gameLoopThread?.running = false
        gameLoopThread?.join()
    }

    fun startGameThread() {
        gameLoopThread = GameLoopThread(this)
        gameLoopThread?.running = true
        gameLoopThread?.start()
    }

    fun startGame() {
        score = 0
        gameStarted = true
        isGameOver = false

        doodler.startGame()
        platformManager.clear()
        platformManager.addPlatforms(NUM_PLATFORMS)
    }

    private fun stopGame() {
        gameStarted = false
        isGameOver = true
        gameLoopThread?.running = false
        gameLoopThread?.join()
    }

    fun update() {
        if (isGameOver) {
            doodler.update()
            return
        }

        if (!gameStarted) {
            if (!doodler.isFalling && !doodler.isJumping) {
                doodler.fall()
            }
            if (doodler.checkCollision(playButton)) {
                doodler.jump()
            }

            playButton.update()
            doodler.update()
            return
        }

        doodler.update()
        if (doodler.y.toInt() < maxHeight) {
            maxHeight = doodler.y.toInt()
        }

        platformManager.updatePlatforms()
        platformManager.checkCollision(doodler)
    }

    private fun gameOver() {
        sounds["gameover"]?.start()
        gameStarted = false
        isGameOver = true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.GREEN)

        val gridHeight: Int = gridImage.height
        val gridWidth: Int = gridImage.width

        for (i in 0..screenHeight / gridHeight) {
            for (j in 0..screenWidth / gridWidth) {
                canvas.drawBitmap(gridImage, j * gridWidth.toFloat(), i * gridHeight.toFloat(), null)
            }
        }

        if (isGameOver) {
            if (doodler.y < screenHeight) {
                doodler.draw(canvas)
                return
            }

            canvas.save()
            canvas.rotate(-10f)
            canvas.drawText("game over", 60f, 400f, textPaint)
            canvas.restore()
            canvas.drawText("You scored $score points!", screenWidth / 2f - 200f, 500f, gameOverScorePaint)
            playButton.buttonText = "Restart"
            playButton.textOffsetY = 18f
            playButton.draw(canvas)
            return
        }

        if (!gameStarted) {
            canvas.save()
            canvas.rotate(-10f)
            canvas.drawText("doodle jump", 60f, 400f, textPaint)
            canvas.restore()

            playButton.buttonText = "Play"
            playButton.textOffsetY = 0f
            playButton.draw(canvas)
            doodler.draw(canvas)
            return
        }

        platformManager.drawPlatforms(canvas)
        doodler.draw(canvas)

        canvas.drawRect(0f, 0f, screenWidth.toFloat(), 100f, scoreHeader)
        canvas.drawText("Score: $score", 60f, 70f, scorePaint)

        val dst = Rect(0, 90, 1080, 90 + 30)
        canvas.drawBitmap(sketchLine, null, dst, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gameStarted) {
            return true
        }

        if (event.action == MotionEvent.ACTION_DOWN) {
            playButton.onTouchEvent(event)
        }
        return true
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isGameOver && event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val xAcc = event.values[0]
            if (xAcc < -0.7) {
                doodler.moveRight()
            } else if (xAcc > 0.7) {
                doodler.moveLeft()
            }
        }
    }

    inner class Doodler { /* CODE ILLUST. SOON*/}
    inner class GameLoopThread(private val gv: View){}
}
