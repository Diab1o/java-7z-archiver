package com.swemel.sevenzip;

public class RefItem implements Comparable {
    private final UpdateItem updateItem;
    private int index;
    private int extensionPos = 0;
    private int namePos = 0;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int extensionIndex = 0;

    public RefItem(int index, UpdateItem ui) {
        this.updateItem = ui;
        this.index = index;
    }

    private static final String g_Exts =
            " lzma 7z ace arc arj bz bz2 deb lzo lzx gz pak rpm sit tgz tbz tbz2 tgz cab ha lha lzh rar zoo" +
                    " zip jar ear war msi" +
                    " 3gp avi mov mpeg mpg mpe wmv" +
                    " aac ape fla flac la mp3 m4a mp4 ofr ogg pac ra rm rka shn swa tta wv wma wav" +
                    " swf " +
                    " chm hxi hxs" +
                    " gif jpeg jpg jp2 png tiff  bmp ico psd psp" +
                    " awg ps eps cgm dxf svg vrml wmf emf ai md" +
                    " cad dwg pps key sxi" +
                    " max 3ds" +
                    " iso bin nrg mdf img pdi tar cpio xpi" +
                    " vfd vhd vud vmc vsv" +
                    " vmdk dsk nvram vmem vmsd vmsn vmss vmtm" +
                    " inl inc idl acf asa h hpp hxx c cpp cxx rc java cs pas bas vb cls ctl frm dlg def" +
                    " f77 f f90 f95" +
                    " asm sql manifest dep " +
                    " mak clw csproj vcproj sln dsp dsw " +
                    " class " +
                    " bat cmd" +
                    " xml xsd xsl xslt hxk hxc htm html xhtml xht mht mhtml htw asp aspx css cgi jsp shtml" +
                    " awk sed hta js php php3 php4 php5 phptml pl pm py pyo rb sh tcl vbs" +
                    " text txt tex ans asc srt reg ini doc docx mcw dot rtf hlp xls xlr xlt xlw ppt pdf" +
                    " sxc sxd sxi sxg sxw stc sti stw stm odt ott odg otg odp otp ods ots odf" +
                    " abw afp cwk lwp wpd wps wpt wrf wri" +
                    " abf afm bdf fon mgf otf pcf pfa snf ttf" +
                    " dbf mdb nsf ntf wdb db fdb gdb" +
                    " exe dll ocx vbx sfx sys tlb awx com obj lib out o so " +
                    " pdb pch idb ncb opt";

    private int getExtIndex(String ext) {
        int extIndex = 1;
        int p = 0;
        for (; ; ) {
            char c = g_Exts.charAt(p++);
            if (g_Exts.length() == p)
                return extIndex;
            if (c == ' ')
                continue;
            int pos = 0;
            for (; ; ) {

                char c2 = ext.charAt(pos++);
                if (ext.length() == pos && (g_Exts.length() - 1 == p || g_Exts.charAt(p) == ' '))
                    return extIndex;
                if (c != c2)
                    break;
                c = g_Exts.charAt(p++);
            }
            extIndex++;
            for (; ; ) {
                if (g_Exts.length() == p)
                    return extIndex;
                if (c == ' ')
                    break;
                c = g_Exts.charAt(p++);
            }
        }
    }

    private int getReverseSlashPos(String name) {
        int slashPos = name.lastIndexOf('/');
        int slash1Pos = name.lastIndexOf('\\');
        slashPos = Math.max(slashPos, slash1Pos);
        return slashPos;
    }

    UpdateItem getUpdateItem() {
        return updateItem;
    }

    public int compareTo(Object o) {
        RefItem ref = (RefItem) o;
        if (this.getUpdateItem().isDir() != ref.getUpdateItem().isDir()) {
            return this.getUpdateItem().isDir() ? 1 : -1;
        }

        return this.getUpdateItem().getName().compareToIgnoreCase(ref.getUpdateItem().getName());


    }
}
