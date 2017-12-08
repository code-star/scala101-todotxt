package todo

import java.io.File
import org.parboiled2.{ ErrorFormatter, ParseError }
import scala.io.{ Source, StdIn }
import scala.util.{ Failure, Success }

object TodoList extends App {
  val tasks = getTasks("todo.txt")
  // ??? pretty print the sequence of tasks
  tasks.foreach(println(_)) // the result will not be human-friendly

  private def getTasks(fileName: String): Seq[Task] = {
    val lines = Source.fromResource(fileName).getLines()

    lines.filter(_ != "").foldLeft(Seq.empty[Task]) {
      case (tasks, s) =>
      val parser = TodoParser(s)

      parser.task.run() match {
        case Failure(error: ParseError) =>
          println(parser.formatError(error, new ErrorFormatter(showTraces = true)))
          tasks
        case Failure(exception) =>
          println(exception)
          tasks
        case Success(task) =>
          tasks :+ task
      }
    }
  }
}
