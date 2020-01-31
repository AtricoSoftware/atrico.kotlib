package atrico.kotlib.konsole.core

/**
 * Strip the ansi codes from a string
 * @return Simple string with no codes
 */
fun String.stripAttributes() = Attribute.stripAttributes(this)

// TODO
///** Parse a string (with ANSI codes) into multi coloured parts
// * @param text The string to parse
// * @return List of partial strings with their corresponding colours
// * */
//fun String.parseColoredText() = Color.parseText(this)
//
/** Extension functions to set attributes on string */
fun String.bold() = Konsole.bold(this)
fun String.dim() = Konsole.dim(this)
fun String.underline() = Konsole.underline(this)
fun String.blink() = Konsole.blink(this)
fun String.invert() = Konsole.invert(this)
fun String.hide() = Konsole.hide(this)
fun String.withAttributes(attributes:Iterable<Attribute>) = Konsole.withAttributes(this, attributes)
fun String.withAttributes(vararg attributes:Attribute) = Konsole.withAttributes(this, attributes.asIterable())

/** Extension functions to set foreground colour of string */
fun String.foregroundColor(color:Color) = Konsole.foreground(this, color)
fun String.black() = Konsole.foreground(this, Color.BLACK)
fun String.red() = Konsole.foreground(this, Color.RED)
fun String.green() = Konsole.foreground(this, Color.GREEN)
fun String.yellow() = Konsole.foreground(this, Color.YELLOW)
fun String.blue() = Konsole.foreground(this, Color.BLUE)
fun String.magenta() = Konsole.foreground(this, Color.MAGENTA)
fun String.cyan() = Konsole.foreground(this, Color.CYAN)
fun String.lightGray() = Konsole.foreground(this, Color.LIGHT_GRAY)
fun String.lightRed() = Konsole.foreground(this, Color.LIGHT_RED)
fun String.lightGreen() = Konsole.foreground(this, Color.LIGHT_GREEN)
fun String.lightYelllow() = Konsole.foreground(this, Color.LIGHT_YELLOW)
fun String.lightBlue() = Konsole.foreground(this, Color.LIGHT_BLUE)
fun String.lightMagenta() = Konsole.foreground(this, Color.LIGHT_MAGENTA)
fun String.lightCyan() = Konsole.foreground(this, Color.LIGHT_CYAN)
fun String.white() = Konsole.foreground(this, Color.WHITE)

/** Extension functions to set background colour of string */
fun String.backgroundColor(color:Color) = Konsole.background(this, color)
fun String.blackBackground() = Konsole.background(this, Color.BLACK)
fun String.redBackground() = Konsole.background(this, Color.RED)
fun String.greenBackground() = Konsole.background(this, Color.GREEN)
fun String.yellowBackground() = Konsole.background(this, Color.YELLOW)
fun String.blueBackground() = Konsole.background(this, Color.BLUE)
fun String.magentaBackground() = Konsole.background(this, Color.MAGENTA)
fun String.cyanBackground() = Konsole.background(this, Color.CYAN)
fun String.lightGrayBackground() = Konsole.background(this, Color.LIGHT_GRAY)
fun String.lightRedBackground() = Konsole.background(this, Color.LIGHT_RED)
fun String.lightGreenBackground() = Konsole.background(this, Color.LIGHT_GREEN)
fun String.lightYellowBackground() = Konsole.background(this, Color.LIGHT_YELLOW)
fun String.lightBlueBackground() = Konsole.background(this, Color.LIGHT_BLUE)
fun String.lightMagentaBackground() = Konsole.background(this, Color.LIGHT_MAGENTA)
fun String.lightCyanBackground() = Konsole.background(this, Color.LIGHT_CYAN)
fun String.whiteBackground() = Konsole.background(this, Color.WHITE)

fun String.colored(foreground:Color, background:Color) = Konsole.color(this, foreground, background)
