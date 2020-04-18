package com.sequoiadb.sql

import com.sequoiadb.utils.Tools.{basePath, readFileForLine}

object DQL {

  def querySql(num: Int): String = {
    readFileForLine(s"${basePath}/gen/sql/${num}.sql")
  }

}
