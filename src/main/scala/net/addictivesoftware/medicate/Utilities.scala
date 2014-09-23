package net.addictivesoftware.medicate

trait Utilities {

  type Closable = {def close(): Unit}
  
  def using[A, B <: Closable] (closeable: B) (f: B => A): A =
    try {
      f(closeable)
    } finally {
      try {
        closeable.close()
      } catch {
        case _: Throwable => //swallow
      }
    }
}

object Utilities extends Utilities