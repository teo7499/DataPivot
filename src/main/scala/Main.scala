import scala.io.StdIn.readLine

final case class ToPivot(col: String, row: String)
//final case class preparedPivot (combination :ToPivot, idx :Int)

object Main {

  def main(args: Array[String]): Unit = {

    val src = io.Source.fromFile("input.csv")
    //Iterator[String] => List[Array[String]]
    val input = src.getLines.map { line => line.split(",") }.toList
    src.close
    print("Available Columns to pivot: " + input(0).mkString(",") + "\nChoose 2 (ColInput,RowInput):")
    val pivotParams = readLine(" ").split(",")
    val result = pivot(input, pivotParams(0), pivotParams(1))
    for (data <- result) println(data.mkString(","))
  }

  def pivot(input: List[Array[String]], colInput: String, rowInput: String): List[Array[String]] = {

    println("Running Pivot")
    //to get index of elements to pivot
    val header = input(0).zipWithIndex

    //get index to filter wanted and unwanted values
    val colIndex = getIndex(header, colInput)
    val rowIndex = getIndex(header, rowInput)

    //List[Array(String)] => List[toPivot]
    val toPivot = input.drop(1).map { i => ToPivot(i(colIndex), i(rowIndex)) }

    //Map(ToPivot(col,row) -> count :Int,
    val pivotCount = toPivot.groupBy(identity).view.mapValues(_.size).toMap
    val pivotHeader = pivotCount.keys.map { key => key.col }.toList.distinct
    val pivotRHeader = pivotCount.keys.map { key => key.row }.toList.distinct

    //zipwithIndex pivotHeader to get row number
    val pivotRowIdx = pivotRHeader.zipWithIndex

    val allCombi = (0 until pivotHeader.size).flatMap {
      i =>
        pivotRowIdx.map {
          case (row, idx) => (ToPivot(pivotHeader(i), row), idx)
          //?((pivotHeader(i),row),idx) IndexedSeq[(String,String),Int]
        }
    }

    val sortedPivot = (0 until pivotRHeader.size).map {
      i =>
        allCombi.flatMap {
          case (p: ToPivot, idx: Int) => if (idx == i) {
            Some(mapMatcher(p, pivotCount))
          }
          else None
        }
    }

    val finalPivot = sortedPivot.zip(pivotRHeader).map {
      case (i, rowHead) => (rowHead +: i.map(_.toString)).toArray
    }.toList

    //Return List((colInput,colHeader,colHeader) , (colRow,i,i), (colRow,i,i)...)
    List(Array(prepareHeader(pivotHeader, rowInput).mkString(",")))
    prepareHeader(pivotHeader, rowInput) +: finalPivot
  }

  def mapMatcher(toMatch: ToPivot, mapMatch: Map[ToPivot, Int]): Int = {
    if (mapMatch.contains(toMatch)) {
      mapMatch.filter(i => i._1 == toMatch).values.head
    }
    else 0
  }

  def prepareData(rowHeader: String, data: IndexedSeq[Int]): IndexedSeq[Any] = {
    rowHeader +: data
  }

  def prepareHeader(pivotHeader: List[String], rowInput: String): Array[String] = {

    rowInput +: (0 until pivotHeader.size).map {
      idx => pivotHeader(idx)
    }.toArray
  }

  def getIndex(i: Array[(String, Int)], j: String): Int = {

    i.flatMap {
      case (header: String, index: Int) => if (header == j) Some(index) else None
    }.head
  }

  def userEntry(userInput: String): Set[String] = {
    //TODO Validate user entry.
    try {
      userInput.split(",").toSet
    }
    catch {
      case _: Throwable => userEntry(readLine("Invalid Input! \nEnter Inputs Again: "))
    }
  }
}