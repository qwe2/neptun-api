package hu.gansperger.neptunapi.transformers

trait Transformer[T] {
  def transform : T
}
