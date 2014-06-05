import java.net.URL;

object DotLlama {
  def main(args: Array[String]) {
    val ix = args.indexOf("--ix") != -1
    if(args.indexOf("--export") != -1 || args.indexOf("-e") != -1){
      export(args.last, ix)
    } else if(args.indexOf("--import") != -1 || args.indexOf("-i") != -1){

    } else {
      println("dot-llama [OPTION]... [url]")
      println("Backup/restore configuration files")
      println("")
      println("  -e, --export    export and symlink configuration files")
      println("  -i, --import    import configuration files")
      println("      --ix        export to ix.io")
      println("  -h, --help      show this help")
    }
  }
  def export(url: String, ix: Boolean) {
    if(ix){

    } else {
      val uri = new URL(url)
      println("Exporting to: "+uri)
    }
  }
}
