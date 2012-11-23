package scala.slick.dao

import scala.reflect.macros.Context

object CopyMacro {

  import scala.language.experimental.macros
  import scala.reflect.macros.Context

  /**
   * COMPILE TIME CHECKING if obj has method 'copy' with parameter 'id'
   * and COMPILE TIME GENERATION of method invocation obj.copy(id = id).
   * Generated COMPILER ERROR if copy method or parameter id is missing.
   */
  def withId[T, I](obj: T, id: I): T = macro withIdImpl[T, I]

  def withIdImpl[T: c.WeakTypeTag, I: c.WeakTypeTag](c: Context)(obj: c.Expr[T], id: c.Expr[I]): c.Expr[T] = {

    import c.universe._

    val tree = reify(obj.splice).tree
    val copy = obj.actualType.member(newTermName("copy"))

    copy match {
      case s: MethodSymbol if (s.paramss.flatten.map(_.name).contains(newTermName("id"))) => c.Expr[T](
        Apply(
          Select(tree, copy),
          AssignOrNamedArg(Ident("id"), reify(id.splice).tree) :: Nil))
      case _ => c.abort(c.enclosingPosition, "No eligible copy method!")
    }

  }

}
