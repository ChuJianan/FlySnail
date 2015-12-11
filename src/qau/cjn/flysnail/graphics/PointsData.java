package qau.cjn.flysnail.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

public class PointsData {
	public float width;
	public float height;
	public Bitmap background;
	public Bitmap rightImage;
	public List<Point> keyPoints = new ArrayList<Point>();
	public List<Point> aidPoints = new ArrayList<Point>();
	
	public PointsData(float width, float height, Bitmap background, Bitmap rightImage, List<Point> keyPoints) {
		this.width = width;
		this.height = height;
		this.background = background;
		this.rightImage = rightImage;
		this.keyPoints = keyPoints;
	}
	
	public PointsData(float width, float height, Bitmap background, Bitmap rightImage, List<Point> keyPoints, List<Point> aidPoints) {
		this.width = width;
		this.height = height;
		this.background = background;
		this.rightImage = rightImage;
		this.keyPoints = keyPoints;
		this.aidPoints = aidPoints;
	}
}
