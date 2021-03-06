import Chapter10Monoids.Monoid
import Chapter10Monoids.Monoid._
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object Chapter10MonoidsTest extends Properties("Chapter10Monoids") {

  /** Exercise 4 */
  def monoidLaws[A](m: Monoid[A], a1: A, a2: A, a3: A): Boolean =
    m.op(a1, m.op(a2, a3)) == m.op(m.op(a1, a2), a3) &&
      m.op(a1, m.zero) == a1 &&
      m.op(m.zero, a1) == a1

  property("monoid laws") = forAll { (i: Int, j: Int, k: Int) =>
    monoidLaws(stringMonoid, i.toString, j.toString, k.toString) &&
      monoidLaws(listMonoid[Int], List(i), List(j), List(k)) &&
      monoidLaws(intAddition, i, j, k) &&
      monoidLaws(intMultiplication, i, j, k) &&
      monoidLaws(booleanAnd, i % 2 == 0, j % 2 == 0, k % 2 == 0)
      monoidLaws(booleanOr, i % 2 == 0, j % 2 == 0, k % 2 == 0) &&
      monoidLaws(optionMonoid[Int], Some(i), Some(j), Some(k))
  }

  property("foldMap") = forAll { (s1: String, s2: String, s3: String) =>
    val as = List(s1, s2, s3)
    val result = s"$s1$s2$s3"

    concatenate(as, stringMonoid) == result &&
      foldMap(as, stringMonoid)(a => a) == result &&
      foldMapViaFoldRight(as, stringMonoid)(a => a) == result &&
      foldRightViaFoldMap(as)("")(_ + _) == result &&
      foldLeftViaFoldMap(as)("")(_ + _) == result &&
      foldMapV(as.toIndexedSeq, stringMonoid)(a => a) == result &&
      foldMapV(IndexedSeq[String](), stringMonoid)(a => a) == stringMonoid.zero
  }

  property("wordCount") =
    wordCount("") == 0 &&
      wordCount("Lorem") == 1 &&
      wordCount("Lorem ipsum dolor sit amet, ") == 5

  property("productMonoid") = forAll { (i: Int, j: Int) =>
    val pMonoid = productMonoid(intAddition, stringMonoid)

    pMonoid.op((i, i.toString), (j, j.toString)) == (i + j, s"$i$j") &&
      pMonoid.zero == (intAddition.zero, stringMonoid.zero)
  }

  property("functionMonoid") = forAll { (i: Int, j: Int) =>
    val f: Int => String = _.toString
    val g: Int => String = i => s"$i$j"
    val fMonoid = functionMonoid[Int, String](stringMonoid)

    fMonoid.op(f, g)(i) == s"$i$i$j" && fMonoid.zero(i) == stringMonoid.zero
  }

}
