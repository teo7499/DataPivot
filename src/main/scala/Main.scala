import scala.io.StdIn.readLine

final case class ToPivot(col: String, row: String)
//final case class preparedPivot (combination :ToPivot, idx :Int)

object Main {

  def main(args: Array[String]): Unit = {

    val src = io.Source.fromFile("input.csv")
    //Iterator[String] => List[Seq[String]]
    val input = src.getLines.map { line => line.split(",").toSeq }.toList
    src.close
    print("Available Columns to pivot: " + input(0).mkString(",") + "\nChoose 2 (ColInput,RowInput):")
    val pivotParams = readLine(" ").split(",")
    val result = pivot(input, pivotParams(0), pivotParams(1))
    for (data <- result) println(data.mkString(","))
  }

  /*def userEntry(userInput: String): Seq[String] = {
      //TODO Validate user entry.
      try {
        userInput.split(",").toSeq
      } catch {
        case _ : Throwable => userEntry(readLine("Invalid Input! \nEnter Inputs Again: "))
      }
    }*/

  def pivot(input: List[Seq[String]], colInput: String, rowInput: String): List[Seq[String]] = {

    println("Running Pivot")
    //to get index of elements to pivot
    val header = input(0)

    //get index to filter wanted and unwanted values
    val colIndex = getIndex(header, colInput)
    val rowIndex = getIndex(header, rowInput)

    //List[IndexedSeq(String)] => List[toPivot]
    val toPivot = input.drop(1).map { i => ToPivot(i(colIndex), i(rowIndex)) }

    //Map(ToPivot(col,row) -> count :Int,
    val pivotCount = toPivot.groupBy(identity).view.mapValues(_.size).toMap
    val pivotHeader = pivotCount.keys.map { key => key.col }.toSeq.distinct
    val pivotRHeader = pivotCount.keys.map { key => key.row }.toList.distinct

    val allCombi = pivotHeader.flatMap {
      colHeader =>
        pivotRHeader.map {
          rowHeader => (ToPivot(pivotHeader(pivotHeader.indexOf(colHeader)), rowHeader), pivotRHeader.indexOf(rowHeader))
        }
    } //List[ToPivot(String,String),Int]

    //returns collection sorted by row index (pivotRHeader(i))
    val sortedPivot = pivotRHeader.map {
      rowHeader =>
        allCombi.flatMap {
          case (p: ToPivot, idx: Int) => if (idx == pivotRHeader.indexOf(rowHeader)) {
            Some(mapMatcher(p, pivotCount))
          }
          else None
        }
    }

    val pivotBody = sortedPivot.map {
      i => prepareData(i, pivotRHeader(sortedPivot.indexOf(i)))
    }

    //Return List((colInput,colHeader,colHeader) , (colRow,i,i), (colRow,i,i)...)
    prepareData(pivotHeader, rowInput) +: pivotBody
  }

  def getIndex(header: Seq[String], colName: String): Int = {
    header.indexOf(colName)
  }

  def mapMatcher(toMatch: ToPivot, mapMatch: Map[ToPivot, Int]): String = {
    mapMatch.getOrElse(toMatch, 0).toString
  }

  def prepareData(genData: Seq[String], input: String): Seq[String] = {
    input +: genData
  }
}
