package qau.cjn.flysnail.util;

import java.io.File;
import java.util.List;

public class NextView {
public List<File> files(String path){
	return DirTraversal.listLinkedFiles(path);
}
}
