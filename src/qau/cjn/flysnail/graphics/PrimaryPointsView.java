package qau.cjn.flysnail.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import qau.cjn.flysnail.util.wutil.BitmapUtil;
import qau.cjn.flysnail.util.wutil.RoundUtil;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PrimaryPointsView extends View {
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
	private List<Point> keyPoints;
	/**
	 * 已连接点
	 */
	private List<List<Point>> sPoints = new ArrayList<List<Point>>();
	private List<Point> tPoints = new ArrayList<Point>();
	/**
	 * 圆点半径(dp)
	 */
	private float mRadius = 8;
	/**
	 * 连线的透明度
	 */
	private int lineAlpha = 50;
	private String key;
	private String sl;
	
	public PrimaryPointsView(Context context) {
		super(context);
		init();
	}
	
	public PrimaryPointsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PrimaryPointsView(Context context, AttributeSet attrs, int defStyleAttr) {
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
	 * 撤销连接线
	 * @return
	 */
	public boolean undo() {
		if (sPoints.size() > 0) {
			sPoints.remove(sPoints.size() - 1);
			this.postInvalidate();
			return true;
		}
		return false;
	}
    @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
		if (!isWin) {
			drawConnLine(canvas);
			drawPoints(canvas);
			drawConnPoints(canvas);
//			mCanvas.drawPath(mPath, mPaint);
		}
	}
	
	private boolean isWin = false;
	public void youWin() {
		isWin = true;
		this.postInvalidate();
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
		int tmpAlpha = mPaint.getAlpha();
		mPaint.setAlpha(lineAlpha);	
		// 画连线 
		// 这是已完成的连接线
		if (sPoints.size() > 0) {
			for(int i=0;i<sPoints.size();i++){
				List<Point> points=sPoints.get(i);
				if (points.size()>0) {									
				Point tp= points.get(0);
				for (int j = 1; j < points.size(); j++) {
					Point p = points.get(j);
					drawLine(canvas, tp, p);
					tp=p;
				}
			}
			}	
		}
		// 当前正在画的连接线
		if (tPoints.size() > 0) {
			Point tp= tPoints.get(0);
			for (int i = 1; i < tPoints.size(); i++) {
				Point p = tPoints.get(i);
				drawLine(canvas, tp, p);
				tp = p;
			}
			if (this.movingNoPoint) {
				drawLine(canvas, tp, new Point((int) moveingX, (int) moveingY));
			}
		}
		mPaint.setAlpha(tmpAlpha);
		lineAlpha = mPaint.getAlpha();
			
//		if(sPoints.size() == keyPoints.size()){
//			Point p1=sPoints.get(0);
//			Point p2=sPoints.get(sPoints.size() - 1);
//			drawLine(canvas, p1, p2);
//			isFinished = true;
//		}
		
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
		}
	}
	/**
	 * 绘制已连接圆点
	 * @param canvas
	 * @param pointMap
	 * @param width		标准宽度
	 */
	private void drawConnPoints(Canvas canvas) {
		for (List<Point> points:sPoints) {				
			for (Point p : points) {
				mPaint.setColor(Color.RED);
				canvas.drawCircle(p.x, p.y, mRadius, mPaint);
			}
		}
		for (Point p : tPoints) {
			mPaint.setColor(Color.RED);
			canvas.drawCircle(p.x, p.y, mRadius, mPaint);
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
		mPaint.setStrokeWidth((float) mRadius * 0.8f);             //设置线宽  
		mPaint.setColor(Color.RED);
		if (this.movingNoPoint) {
			canvas.drawLine(a.x, a.y, b.x, b.y, mPaint);//画线
		}else {
			canvas.drawLine(a.x, a.y, b.x, b.y, mPaint);//画线	
		}	
	}
	/**
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}
	boolean movingNoPoint = false;
	float moveingX, moveingY;
	float endX,endY;
	int iclear=0;
	Point iPoint=null;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isWin) {
			return false;
		}
		float ex = event.getX();
		float ey = event.getY();
		Point p = null;
		movingNoPoint=false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 点下
			//reset();
			tPoints=new ArrayList<Point>();
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
			if (isFinished && mCompleteListener != null) {
				mCompleteListener.onComplete(toPointString());
				return false;
			}
			this.sPoints.add(tPoints);
			tPoints=new ArrayList<Point>();//重置当前连接点
			break;
		}
		if ( p != null) {
			addPoint(p); //不计算重叠 连接每个点
//			int rk = crossPoint(p);
//			if (rk == 2)  { // 与非最后一重叠
//				// reset();
//				// checking = false;
//
//				movingNoPoint=true;
//				moveingX = ex;
//				moveingY = ey;
//			} else if (rk == 0) { // 一个新点
//				p.state = Point.STATE_CHECK;
//				addPoint(p);
//			}
//			// rk == 1 不处理

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
		this.tPoints.add(point);
		
		
	}
	/**
	 * 判断点是否有交叉 返回 0,新点 ,1 与上一点重叠 2,与非最后一点重叠
	 * 
	 * @param p
	 * @return
	 */
//	private int crossPoint(Point p) {
//		// 重叠的不最后一个则 reset
//		if (sPoints.contains(p)) {
//			if (sPoints.size() > 2) {
//				// 与非最后一点重叠
//				if (sPoints.get(sPoints.size() - 1).index != p.index) {
//					return 2;
//				}
//			}
//			return 1; // 与最后一点重叠
//		} else {
//			return 0; // 新点
//		}
//	}
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
			for(List<Point>points:sPoints){
			for (Point p : points) {
				sf.append(",");
				sf.append(p.index);
			}
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
			if (RoundUtil.checkInRound(p.x, p.y, 40, (int) x, (int) y)) {
				return p;
			}
		}
		return null;
	}
//	public boolean verifyPassword(String password) {
//		boolean verify = false;
//		for(int i = 0; i < sPoints.size(); i++){
//			Point point = sPoints.get(i);
//			if (i == 0) {
//				sl = point.index.toString();
//			} else {
//				sl += point.index;
//			}
//		}
//		if (key.equals(sl)) {
//			verify = true;
//		}
//		return verify;
//	}
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
