import java.net.URL
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import scala.sys.process._

object DotLlama {
  def main(args: Array[String]) {
    val ix = args.indexOf("--ix") != -1
    if(args.indexOf("--add") != -1 || args.indexOf("-a") != -1){
      export(args.last, ix)
    } else if(args.indexOf("--export") != -1 || args.indexOf("-e") != -1){
      val repo = args.last.trim
      val date = "date".!!.trim.replace(" ", "\\ ")
      val file_home = System.getenv("HOME")+"/.llama/files"
      val commands = Array("git remote add origin "+repo,
        "git add -A",
        "git commit",
        "git push -u origin master")
      for(cmd <- commands zipWithIndex){
        val proc = sys.process.Process(cmd._1, new File(file_home))
        proc.!
      }
    } else if(args.indexOf("--import") != -1 || args.indexOf("-i") != -1){
    } else {
      println("dot-llama [OPTION]... [url]")
      println("Backup/restore configuration files")
      println("")
      println("  -a, --add       add and symlink configuration files")
      println("  -e, --export    export to outside provider (default github)")
      println("  -i, --import    import configuration files")
      println("      --ix        export to ix.io")
      println("  -h, --help      show this help")
    }
  }
  def isSymlink(file: java.io.File):Boolean = {
    !file.getAbsolutePath().equals(file.getCanonicalPath())
  }
  def makeFolder() {
    val home = System.getenv("HOME")
    val config_home = new File(home+"/.llama")
    if(!config_home.exists){
      println("Making .llama config_home")
      config_home.mkdir()
    }
    val gitdir = new File(home+"/.llama/files")
    if(!gitdir.exists){
      println("Making .llama/files dir")
      gitdir.mkdir()
    }
    if(!new File(home+"/.llama/files/.git").exists){
      ("git init "+home+"/.llama/files").!
    }
  }
  def getFiles():Array[java.io.File] = {
    val home = System.getenv("HOME")
    val list = (new File(home)).listFiles
      .filter(f => """^\.""".r.findFirstIn(f.getName).isDefined)
      .filter(f => !isSymlink(f))
    makeFolder()
    //val file = new File(home+"/.llama/config")
    val file = File.createTempFile("dot-llama",".conf", new File("/home/rice/.llama"))
    file.deleteOnExit()
    //if(!file.exists){
    file.createNewFile
    val writer = new PrintWriter(file, "UTF-8");
    writer.println("# Uncomment/add files to back them up.")
    for(f <- list zipWithIndex){
      if(f._1.getName != ".llama"){
        writer.println("# "+f._1.getName);
      }
    }
    writer.close();
    //}
    val config_file = file.getAbsolutePath
    val pb = Process("/usr/bin/gvim "+config_file)
    pb.!

    val source = scala.io.Source.fromFile(config_file)
    val lines = source.mkString.split("\n")
    source.close()
    var files = lines.map(x => x.split("#")(0).trim).filter(x => x.length > 0).map(f => new File(home+"/"+f))
    println(files.mkString("[", ", ", "]"))
    for(f <- files zipWithIndex){
      if(!isSymlink(f._1)){
        val n_file = new File(home+"/.llama/files/"+f._1.getName)
        val n_path = n_file.toPath
        val c_path = f._1.toPath
        f._1.renameTo(n_file)
        Files.createSymbolicLink(c_path, n_path)
        println("Created link for: "+f._1.getName)
      }
    }
    list
  }
  def export(url: String, ix: Boolean) {
    val files = getFiles
    if(ix){

    } else {
      println("Exporting to GitHub")
    }
  }
}
