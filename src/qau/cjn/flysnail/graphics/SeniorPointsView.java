package qau.cjn.flysnail.graphics;

import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.util.wutil.BitmapUtil;
import qau.cjn.flysnail.util.wutil.MathUtil;
import qau.cjn.flysnail.util.wutil.RoundUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SeniorPointsView extends View {
	private OnCompleteListener mCompleteListener;
	/**
	 * 是否完成连接点
	 */
	private boolean isFinished = false;
	/**
	 * 是否已设置数据
	 */
	private boolean hasData = false;
	/**
	 * 
	 */
	private boolean assist=false;
	/**
	 * 是否初始化完毕
	 */
	private boolean isInited = false;
	/**
	 * 标准宽度
	 */
	private float mWidth;
	/**
	 * 标准高度
	 */
	private float mHeight;
	/**
	 * 背景
	 */
	private Bitmap mBackground;
	/**
	 * 正确图片
	 */
	private Bitmap mRightImage;
	/**
	 * 画笔
	 */
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	/**
	 * 关键点阵
	 */
	private List<Point> keyPoints = new ArrayList<Point>();
	/**
	 * 辅助点阵
	 */
	private List<Point> aidPoints = new ArrayList<Point>();
	/**
	 * 已连接点
	 */
	private List<Point> sPoints = new ArrayList<Point>();
	/**
	 * 圆点半径(dp)
	 */
	private float mRadius = 11/2;
	/**
	 * 连线的透明度
	 */
	private int lineAlpha = 50;
	private String key;
	private String sl;
	
	public SeniorPointsView(Context context) {
		super(context);
		init();
	}
	
	public SeniorPointsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SeniorPointsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		mPaint.setColor(Color.BLACK);
		float scale = getResources().getDisplayMetrics().density;
		mRadius = (int) (mRadius * scale + 0.5f);
	}
	
	/**
	 * 初始化绘点数据
	 */
	private void initPoints() {
		for (int i = 0; i < keyPoints.size(); i++) {
			Point p = keyPoints.get(i);
			p.x = p.x * (float) getWidth() / mWidth;
			p.y = p.y * (float) getHeight() / mHeight;
			if(key == null){
				key = p.index.toString();
			}else{
				key += p.index;
			}
			keyPoints.set(i, p);
		}
		for (int i = 0; i < aidPoints.size(); i++) {
			Point p = aidPoints.get(i);
			p.x = p.x * (float) getWidth() / mWidth;
			p.y = p.y * (float) getHeight() / mHeight;
			aidPoints.set(i, p);
		}
		if (mBackground != null) {
			float sf = (float) getWidth() / mBackground.getWidth(); // 取得缩放比例，将所有的图片进行缩放
			mBackground = BitmapUtil.zoom(mBackground, sf);
			
		}
		if (mRightImage != null) {
			float sf = (float) getWidth() / mRightImage.getWidth(); // 取得缩放比例，将所有的图片进行缩放
			mRightImage = BitmapUtil.zoom(mRightImage, sf);
		}
		
		isInited = true;
	}
	
	/**
	 * 设置绘制点数据
	 * @param w	标准宽度
	 * @param h	标准高度
	 * @param pointMap
	 */
	public void setDatas(PointsData data) {
		mWidth = data.width;
		mHeight = data.height;
		keyPoints = data.keyPoints;
		aidPoints = data.aidPoints;
		mBackground = data.background;
		mRightImage = data.rightImage;
		hasData = true;
	}
	
	/**
	 * 重置
	 */
	public void reset() {
		this.sPoints.clear();
		isFinished = false;
		isWin = false;
		this.postInvalidate();
	}
	
	/**
	 * 撤销连接点
	 * @return
	 */
	public boolean undo() {
		if (isWin) {
			return false;
		}
		if (sPoints.size() > 0) {
			sPoints.remove(sPoints.size() - 1);
			isFinished = false;
			this.postInvalidate();
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否连接了辅助点
	 */
	public boolean checkassist(){
		if (assist) {
			return false;
		}else{
		return true;
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!hasData) {
			return;
		}
		if (!isInited) {
			initPoints();
		}
		
		drawBackground(canvas);
		drawRightImage(canvas);
		drawConnLine(canvas);
		drawPoints(canvas);
		drawConnPoints(canvas);
	}
	
	private boolean isWin = false;
	public void youWin() {
		isWin = true;
	}
	
	int cc = 0;
	/**
	 * 绘制正确图片
	 * @param canvas
	 */
	private void drawRightImage(Canvas canvas) {
		if (mRightImage == null) {
			return;
		}
		if (isWin) {
			if (cc < 255) {
				cc = cc + 50;
			}
			if (cc > 255) {
				cc = 255;
			}
			mPaint.setAlpha(cc);
			canvas.drawBitmap(mRightImage, 0, 0, mPaint);
			this.postInvalidateDelayed(100);
		}
	}
	
	/**
	 * 绘制背景
	 * @param canvas
	 */
	private void drawBackground(Canvas canvas) {
		if (mBackground == null) {
			return;
		}
		canvas.drawBitmap(mBackground, 0, 0, mPaint);
	}
	
	/**
	 * 绘制连接线
	 * @param canvas
	 */
	private void drawConnLine(Canvas canvas) {
		// 画连线
		if (sPoints.size() > 0) {
			int tmpAlpha = mPaint.getAlpha();
			mPaint.setAlpha(lineAlpha);
			Point tp = sPoints.get(0);
			for (int i = 1; i < sPoints.size(); i++) {
				Point p = sPoints.get(i);
				drawLine(canvas, tp, p);
				tp = p;
			}
			if (this.movingNoPoint) {
				drawLine(canvas, tp, new Point((int) moveingX, (int) moveingY));
			}
			mPaint.setAlpha(tmpAlpha);
			lineAlpha = mPaint.getAlpha();
		}
		if (sPoints.size() > 0) {
			Point p1 = sPoints.get(sPoints.size() - 1);
			Point p2 = keyPoints.get(keyPoints.size() - 1);
			if (p1 == p2) {
				isFinished = true;
			}
		}
	}
	
	/**
	 * 绘制圆点
	 * @param canvas
	 * @param pointMap
	 * @param width		标准宽度
	 */
	private void drawPoints(Canvas canvas) {
		float cx, cy;
		for (int i = 0; i < keyPoints.size(); i++) {
			Point p = keyPoints.get(i);
			cx = p.x;
			cy = p.y;
			mPaint.setColor(Color.BLACK);
			canvas.drawCircle(cx, cy, mRadius, mPaint);
//			mPaint.setColor(Color.WHITE);
//			mPaint.setTextSize(mRadius);
//			mPaint.setTextAlign(Align.CENTER);
//			canvas.drawText(p.index.toString(), cx, cy + mRadius / 3, mPaint);
		}
		for (int i = 0; i < aidPoints.size(); i++) {
			Point p = aidPoints.get(i);
			cx = p.x;
			cy = p.y;
			mPaint.setColor(Color.BLACK);
			canvas.drawCircle(cx, cy, mRadius, mPaint);
//			mPaint.setColor(Color.WHITE);
//			mPaint.setTextSize(mRadius);
//			mPaint.setTextAlign(Align.CENTER);
//			canvas.drawText(p.index.toString(), cx, cy + mRadius / 3, mPaint);
		}
	}
	/**
	 * 绘制已连接圆点
	 * @param canvas
	 * @param pointMap
	 * @param width		标准宽度
	 */
	private void drawConnPoints(Canvas canvas) {
		for (Point p : sPoints) {
			mPaint.setColor(Color.RED);
			canvas.drawCircle(p.x, p.y, mRadius, mPaint);
			mPaint.setColor(Color.WHITE);
			mPaint.setTextSize(mRadius);
			mPaint.setTextAlign(Align.CENTER);
			canvas.drawText(p.index.toString(), p.x, p.y + mRadius / 3, mPaint);
		}
	}
	/**
	 * 画两点的连接
	 * 
	 * @param canvas
	 * @param a
	 * @param b
	 */
	private void drawLine(Canvas canvas, Point a, Point b) {
		float ah = (float) MathUtil.distance(a.x, a.y, b.x, b.y);
		//float degrees = getDegrees(a, b);
		// Log.d("=============x===========", "rotate:" + degrees);
		//canvas.rotate(degrees, a.x, a.y);
		mPaint.setStrokeWidth((float) mRadius * 0.8f);             //设置线宽  
		mPaint.setColor(Color.RED);
		canvas.drawLine(a.x, a.y, b.x, b.y, mPaint);//画线
		
		//canvas.rotate(-degrees, a.x, a.y);
	}
	/**
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}
	boolean movingNoPoint = false;
	float moveingX, moveingY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isWin) {
			return false;
		}
		float ex = event.getX();
		float ey = event.getY();
		Point p = null;
		movingNoPoint=false;
		assist=false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 点下
			//reset();
			p = checkSelectPoint(ex, ey);
			break;
		case MotionEvent.ACTION_MOVE: // 移动
			p = checkSelectPoint(ex, ey);
			if (p == null) {
				movingNoPoint=true;
				moveingX = ex;
				moveingY = ey;
			}
			break;
		case MotionEvent.ACTION_UP: // 提起
			//p = checkSelectPoint(ex, ey);
			if (isFinished) {
				mCompleteListener.onComplete(toPointString());
				return false;
			}
			break;
		}
		if ( p != null) {

			int rk = crossPoint(p);
			if (rk == 2)  { // 与非最后一重叠
				// reset();
				// checking = false;

				movingNoPoint=true;
				moveingX = ex;
				moveingY = ey;
			} else if (rk == 0) { // 一个新点
				p.state = Point.STATE_CHECK;
				addPoint(p);
			}
			// rk == 1 不处理

		}
		this.postInvalidate();
		return hasData;
		
	}
	

	/**
	 * 添加一个点
	 * 
	 * @param point
	 */
	private void addPoint(Point point) {
		this.sPoints.add(point);
	}
	/**
	 * 判断点是否有交叉 返回 0,新点 ,1 与上一点重叠 2,与非最后一点重叠
	 * 
	 * @param p
	 * @return
	 */
	private int crossPoint(Point p) {
		// 重叠的不最后一个则 reset
		if (sPoints.contains(p)) {
			if (sPoints.size() > 2) {
				// 与非最后一点重叠
				if (sPoints.get(sPoints.size() - 1).index != p.index) {
					return 2;
				}
			}
			return 1; // 与最后一点重叠
		} else {
			return 0; // 新点
		}
	}
	/**
	 * 取得密码
	 * 
	 * @return
	 */
	private String getPassword() {
		SharedPreferences settings = this.getContext().getSharedPreferences(
				this.getClass().getName(), 0);
		return settings.getString("password", ""); // , "0,1,2,3,4,5,6,7,8"
	}
	/**
	 * 转换为String
	 * 
	 * @param points
	 * @return
	 */
	private String toPointString() {
		if (sPoints.size() == keyPoints.size()) {
			StringBuffer sf = new StringBuffer();
			for (Point p : sPoints) {
				sf.append(",");
				sf.append(p.index);
			}
			return sf.deleteCharAt(0).toString();
		} else {
			return "";
		}
	}
	/**
	 * 
	 * 检查
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Point checkSelectPoint(float x, float y) {
		for (int i = 0; i < keyPoints.size(); i++) {
			Point p = keyPoints.get(i);
			if (RoundUtil.checkInRound(p.x, p.y, 80, (int) x, (int) y)) {
				return p;
			}
		}
		for (int i = 0; i < aidPoints.size(); i++) {
			Point p = aidPoints.get(i);
			if (RoundUtil.checkInRound(p.x, p.y, 20, (int) x, (int) y)) {
				assist=true;
				return p;
			}
		}
		return null;
	}
	public boolean verifyPassword(String password) {
		boolean verify = false;
		for(int i = 0; i < sPoints.size(); i++){
			Point point = sPoints.get(i);
			if (i == 0) {
				sl = point.index.toString();
			} else {
				sl += point.index;
			}
		}
		if (key.equals(sl)) {
			verify = true;
		}
		return verify;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		if (mBackground != null && !mBackground.isRecycled()) {
			mBackground.recycle();
		}
		if (mRightImage != null && !mRightImage.isRecycled()) {
			mRightImage.recycle();
		}
	}
	/**
	 * 轨迹球画完成事件
	 * 
	 * @author way
	 */
	public interface OnCompleteListener {
		/**
		 * 画完了
		 * 
		 * @param str
		 */
		public void onComplete(String password);
	}
}
