package com.swemel.sevenzip;

import java.io.File;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 17.02.2011
 * Time: 15:15:32
 * To change this template use File | Settings | File Templates.
 */
public class ArchiveUpdateCallback {
    Vector<Long> volumesSizes;
      String volName;
      String volExt;

      String dirPrefix;
      Vector<File> dirItems;

      boolean passwordIsDefined;
      String password;
      boolean askPassword;

      boolean m_NeedBeClosed;

      Vector<String> failedFiles;
      Vector<Long> failedCodes;

      public ArchiveUpdateCallback(){
          passwordIsDefined=false;
          askPassword=false;
          dirItems=new Vector<File>();
      }


      void Init(Vector<File> dirItems)
      {
        this.dirItems = dirItems;
        m_NeedBeClosed = false;
        failedFiles.clear();
        failedCodes.clear();
      }







}
