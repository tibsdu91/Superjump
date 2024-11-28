package com.doodlejump

data class Vector(var x: Float, var y: Float) {

    operator fun plus(vec: Vector): Vector {
        return Vector(x + vec.x, y + vec.y)
    }

    operator fun minus(vec: Vector): Vector {
        return Vector(x - vec.x, y - vec.y)
    }

    operator fun times(a: Float): Vector {
        return Vector(x * a, y * a)
    }

    operator fun div(a: Float): Vector {
        return Vector(x / a, y / a)
    }

    operator fun get(i: Int): Float {
        return if(i == 0) x else y
    }

    operator fun set(i: Int, a: Float) {
        if(i == 0) x = a else y = a
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}