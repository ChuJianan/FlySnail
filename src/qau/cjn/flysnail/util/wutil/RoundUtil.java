package qau.cjn.flysnail.util.wutil;

public class RoundUtil {
	/**
	 * 点在圆肉
	 * 
	 * @param sx
	 * @param sy
	 * @param r
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkInRound(float sx, float sy, float r, float x,
			float y) {
		double i=Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y));
		return  i< r;
	}
}
