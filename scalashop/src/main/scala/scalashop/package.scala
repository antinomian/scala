package object scalashop {

  /** The value of every pixel is represented as a 32 bit integer. */
  type RGBA = Int

  /** Returns the red component. */
  def red(c: RGBA): Int = (0xff000000 & c) >>> 24

  /** Returns the green component. */
  def green(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  /** Returns the blue component. */
  def blue(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  /** Returns the alpha component. */
  def alpha(c: RGBA): Int = (0x000000ff & c) >>> 0

  /** Used to create an RGBA value from separate components. */
  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (r << 24) | (g << 16) | (b << 8) | (a << 0)
  }

  /** Restricts the integer into the specified range. */
  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  /** Image is a two-dimensional matrix of pixel values. */
  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))
    def apply(x: Int, y: Int): RGBA = data(y * width + x)
    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  /** Computes the blurred RGBA value of a single pixel of the input image. */
  def boxBlurKernel(src: Img, x: Int, y: Int, radius: Int, pb: Boolean = false): RGBA = {
    var r = 0
    var g = 0
    var b = 0
    var a = 0
    var p = 0

    for(i <- math.max(0,x-radius) to math.min(x+radius, src.width-1)){
      for(j <- math.max(0,y-radius) to math.min(y+radius, src.height-1)){
        val curr = src(i,j)
        r += red(curr)
        g += green(curr)
        b += blue(curr)
        a += alpha(curr)
        p += 1
      }
    }

    r = r/p
    g = g/p
    b = b/p
    a = a/p

    rgba(clamp(r, 0, 255),
      clamp(g, 0, 255),
      clamp(b, 0, 255),
      clamp(a, 0, 255))
  }
}
