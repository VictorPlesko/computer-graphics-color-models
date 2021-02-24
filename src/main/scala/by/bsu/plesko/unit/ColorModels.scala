package by.bsu.plesko.unit

object ColorModels {
  sealed abstract case class RGB private(r: Int, g: Int, b: Int) {
    def toCMYK: CMYK = {
      val K = (1d - r / 255d) min (1d - g / 255d) min (1d - b / 255d)
      val C = (1d - r / 255d - K) / (1d - K)
      val M = (1d - g / 255d - K) / (1d - K)
      val Y = (1d - b / 255d - K) / (1d - K)
      CMYK.create((C * 100).round.toInt, (M * 100).round.toInt, (Y * 100).round.toInt, (K * 100).round.toInt)
    }

    def toXYZ: XYZ = {
      def F(x: Double): Double = if (x >= 0.04045) Math.pow((x + 0.055) / 1.055, 2.4) else x / 12.92

      val coefArr = Vector(
        Vector(0.412453, 0.357580, 0.180423),
        Vector(0.212671, 0.715160, 0.072169),
        Vector(0.019334, 0.119193, 0.950227)
      )

      val Rn = F(r / 255d) * 100
      val Gn = F(g / 255d) * 100
      val Bn = F(b / 255d) * 100

      val result = multipleMatrix(coefArr, Vector(Rn, Gn, Bn))
      XYZ.create(result(0).round.toInt, result(1).round.toInt, result(2).round.toInt)
    }
  }

  object RGB {
    def create(r: Int, g: Int, b: Int): RGB = {
      val verifiedParam = List(r, g, b).map(color => if (color < 0) 0 else if (color > 255) 255 else color)
      new RGB(verifiedParam.head, verifiedParam.tail.head, verifiedParam.last) {}
    }
  }

  sealed abstract case class CMYK private(c: Int, m: Int, y: Int, k: Int) {
    def toRGB: RGB = {
      val r = 255 * (1 - c / 100d) * (1 - k / 100d)
      val g = 255 * (1 - m / 100d) * (1 - k / 100d)
      val b = 255 * (1 - y / 100d) * (1 - k / 100d)

      RGB.create(r.round.toInt, g.round.toInt, b.round.toInt)
    }
  }

  object CMYK {
    def create(c: Int, m: Int, y: Int, k: Int): CMYK = {
      val verifiedParam = List(c, m, y, k).map(color => if (color < 0) 0 else if (color > 100) 100 else color).toVector
      new CMYK(verifiedParam(0), verifiedParam(1), verifiedParam(2), verifiedParam(3)) {}
    }
  }

  sealed abstract case class LAB private(l: Int, a: Int, b: Int) {
    def toXYZ: XYZ = {
      def F(x: Double): Double = if (Math.pow(x, 3) >= 0.008856) Math.pow(x, 3) else (x - 16 / 116) / 7.787

      val Xw = 95.047
      val Yw = 100d
      val Zw = 108.883

      val y = F((l + 16d) / 116d) * Xw
      val x = F(a / 500d + (l + 16d) / 116d) * Yw
      val z = F((l + 16d) / 116d - b / 200d) * Zw

      XYZ.create(x.round.toInt, y.round.toInt, z.round.toInt)
    }
  }

  object LAB {

    def create(l: Int, a: Int, b: Int): LAB = {
      val verifiedL = if (l < 0) 0 else if (l > 100) 100 else l
      val verifiedOtherParam = List(a, b).map(param => if (param < -128) -128 else if (param > 128) 128 else param)
      new LAB(verifiedL, verifiedOtherParam.head, verifiedOtherParam.last) {}
    }
  }

  sealed abstract case class XYZ private(x: Int, y: Int, z: Int) {
    def toLab: LAB = {
      def F(x: Double): Double = if (x >= 0.008856) Math.cbrt(x) else 7.787 * x + (16 / 116)

      val Xw = 95.047
      val Yw = 100d
      val Zw = 108.883

      val L = 116 * F(y / Yw) - 16
      val a = 500 * (F(x / Xw) - F(y / Yw))
      val b = 200 * (F(y / Yw) - F(z / Zw))

      LAB.create(L.round.toInt, a.round.toInt, b.round.toInt)
    }

    def toRGB: RGB = {
      def F(x: Double): Double = if (x >= 0.0031308) 1.055 * Math.pow(x, 1 / 2.4) - 0.055 else 12.92 * x

      val defCoef = Vector(
        Vector(3.2406, -1.5372, -0.4986),
        Vector(-0.9689, 1.8758, 0.0415),
        Vector(0.0557, -0.2040, 1.0570)
      )

      val resultN = multipleMatrix(defCoef, Vector(x / 100d, y / 100d, z / 100d))

      val resultRGB = resultN.map(F(_) * 255)

      RGB.create(resultRGB(0).round.toInt, resultRGB(1).round.toInt, resultRGB(2).round.toInt)
    }
  }

  object XYZ {
    def create(x: Int, y: Int, z: Int): XYZ = {

      val verifiedX = if (x < 0) 0 else if (x > 95) 95 else x
      val verifiedY = if (y < 0) 0 else if (y > 100) 100 else y
      val verifiedZ = if (z < 0) 0 else if (z > 108) 108 else z

      new XYZ(verifiedX, verifiedY, verifiedZ) {}
    }
  }

  private def multipleMatrix(coefVector: Vector[Vector[Double]], nVector: Vector[Double]): Vector[Double] = {
    coefVector.foldLeft(Vector.empty[Double])((acc, line) =>
      acc :+ (line zip nVector).map(elementByElment => elementByElment._1 * elementByElment._2).sum)
  }
}
