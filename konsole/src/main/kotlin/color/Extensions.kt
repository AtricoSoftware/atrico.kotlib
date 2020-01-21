package atrico.kotlib.konsole.color

import atrico.kotlib.konsole.color.Color

/**
 * Strip the color codes from a string
 * @return Simple string with no colors
 */
@JvmName("stripColors_StringExtension")
fun String.stripColors() = Color.stripColors(this)

/** Parse a string (with ANSI codes) into multi coloured parts
 * @param text The string to parse
 * @return List of partial strings with their corresponding colours
 * */
fun String.parseColoredText() = Color.parseText(this)

/** Extension functions to set foreground colour of string */
fun String.black() = Color.foreground(this, Color.BLACK)

fun String.red() = Color.foreground(this, Color.RED)
fun String.green() = Color.foreground(this, Color.GREEN)
fun String.yellow() = Color.foreground(this, Color.YELLOW)
fun String.blue() = Color.foreground(this, Color.BLUE)
fun String.magenta() = Color.foreground(this, Color.MAGENTA)
fun String.cyan() = Color.foreground(this, Color.CYAN)
fun String.lightGray() = Color.foreground(this, Color.LIGHT_GRAY)
fun String.lightRed() = Color.foreground(this, Color.LIGHT_RED)
fun String.lightGreen() = Color.foreground(this, Color.LIGHT_GREEN)
fun String.lightYelllow() = Color.foreground(this, Color.LIGHT_YELLOW)
fun String.lightBlue() = Color.foreground(this, Color.LIGHT_BLUE)
fun String.lightMagenta() = Color.foreground(this, Color.LIGHT_MAGENTA)
fun String.lightCyan() = Color.foreground(this, Color.LIGHT_CYAN)
fun String.white() = Color.foreground(this, Color.WHITE)

/** Extension functions to set foreground colour of string */
fun String.blackBackground() = Color.background(this, Color.BLACK)

fun String.redBackground() = Color.background(this, Color.RED)
fun String.greenBackground() = Color.background(this, Color.GREEN)
fun String.yellowBackground() = Color.background(this, Color.YELLOW)
fun String.blueBackground() = Color.background(this, Color.BLUE)
fun String.magentaBackground() = Color.background(this, Color.MAGENTA)
fun String.cyanBackground() = Color.background(this, Color.CYAN)
fun String.lightGrayBackground() = Color.background(this, Color.LIGHT_GRAY)
fun String.lightRedBackground() = Color.background(this, Color.LIGHT_RED)
fun String.lightGreenBackground() = Color.background(this, Color.LIGHT_GREEN)
fun String.lightYellowBackground() = Color.background(this, Color.LIGHT_YELLOW)
fun String.lightBlueBackground() = Color.background(this, Color.LIGHT_BLUE)
fun String.lightMagentaBackground() = Color.background(this, Color.LIGHT_MAGENTA)
fun String.lightCyanBackground() = Color.background(this, Color.LIGHT_CYAN)
fun String.whiteBackground() = Color.background(this, Color.WHITE)
