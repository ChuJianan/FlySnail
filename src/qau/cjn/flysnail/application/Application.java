package qau.cjn.flysnail.application;

import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application{

	private List<String > path=new ArrayList<String>();

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}
	
}
