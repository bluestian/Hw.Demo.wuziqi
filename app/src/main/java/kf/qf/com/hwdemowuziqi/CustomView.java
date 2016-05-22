package kf.qf.com.hwdemowuziqi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by 84903 on 2016/5/7.
 */
public class CustomView  extends View {
    private  int   MAX_LINE ;//棋盘的行列数
    private  int mpanelWidth;//棋盘的宽度；
    private  float mLineHeight;//棋盘的单行间距；
    private Paint mPaint = new Paint();
    private int mPanelLineColor;    //棋盘线的颜色

    private Bitmap mWhitePiece;     //白棋的图片
    private Bitmap mBlackPiece;     //黑棋的图片
    private int MAX_COUNT_IN_LINE ;
    //棋子占行距的比例
    private final float RATIO_PIECE_OF_LINE_HEIGHT = 3 * 1.0f / 4;
    //是否将要下白棋
    private boolean mIsWhite = true;
    //已下的白棋的列表
    private ArrayList<Point> mWhitePieceArray = new ArrayList<>();
    //已下的黑棋的列表
    private ArrayList<Point> mBlackPieceArray = new ArrayList<>();
    public CustomView(Context context) {
        this(context, null);
    }
    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
       // setBackgroundColor(0x44ff0000);

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取xml中自定义的属性值并对相应的属性赋值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        int n = a.getIndexCount();
        for (int i = 0;i < n; i++) {
            int attrName = a.getIndex(i);
            switch (attrName) {
                //棋盘背景
                case R.styleable.CustomView_panel_background:
                    BitmapDrawable panelBackgroundBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    setBackground(panelBackgroundBitmap);
                    break;
                //棋盘线的颜色
                case R.styleable.CustomView_panel_line_color:
                    mPanelLineColor = a.getColor(attrName, 0x88000000);
                    break;
                //白棋图片
                case R.styleable.CustomView_white_piece_img:
                    BitmapDrawable whitePieceBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    mWhitePiece = whitePieceBitmap.getBitmap();
                    break;
                //黑棋图片
                case R.styleable.CustomView_black_piece_img:
                    BitmapDrawable blackPieceBitmap = (BitmapDrawable) a.getDrawable(attrName);
                    mBlackPiece = blackPieceBitmap.getBitmap();
                    break;
                case R.styleable.CustomView_max_count_line:
                    MAX_LINE = a.getInteger(attrName, 10);
                    break;
                case R.styleable.CustomView_max_win_count_piece:
                    MAX_COUNT_IN_LINE = a.getInteger(attrName, 5);
                    break;
            }
        }

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
           int widthsize = MeasureSpec.getSize(widthMeasureSpec);
           int widthmode = MeasureSpec.getMode(widthMeasureSpec);

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);

         int width = Math.min(widthsize, heightsize);
        if (widthmode==MeasureSpec.UNSPECIFIED){
               width = heightsize;
        }
           else if (heightmode==MeasureSpec.UNSPECIFIED){
                 width = widthsize;

        }
         setMeasuredDimension(width, width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        mpanelWidth = w;
        mLineHeight = (int) (mpanelWidth*1.0f/MAX_LINE);
        int pieceWidth = (int) (mLineHeight * RATIO_PIECE_OF_LINE_HEIGHT);
       mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
                int action = event.getAction();
             if (action==MotionEvent.ACTION_UP){
                   int x = (int) event.getX();
                   int y = (int) event.getY();
                 Point mpoint =getValidPoint(x, y);
              if (mWhitePieceArray.contains(mpoint)||mBlackPieceArray.contains(mpoint)){
                         return  false;
              }
                 if (mIsWhite){
                      mWhitePieceArray.add(mpoint);
                 }
                   else {
                       mBlackPieceArray.add(mpoint);
                 }
                   invalidate();
                 mIsWhite = !mIsWhite;
                 return true;
             }
             return  true;

    }
    //根据触摸点获取最近的格子位置
    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight),(int)(y / mLineHeight));
    }

    private void init() {
        mPaint.setColor(mPanelLineColor);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStyle(Paint.Style.FILL);
        if (mWhitePiece == null) {
            mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        }
        if (mBlackPiece == null) {
            mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
    }
    //绘制棋子；
    private void drawPiece(Canvas canvas) {
           for (int i = 0,n = mWhitePieceArray.size();i < n;i++){
                  Point mWhitePoint = mWhitePieceArray.get(i);
                canvas.drawBitmap(mWhitePiece,
                        (mWhitePoint.x+(1-RATIO_PIECE_OF_LINE_HEIGHT)/2)*mLineHeight,
                        ( mWhitePoint.y+(1-RATIO_PIECE_OF_LINE_HEIGHT)/2)*mLineHeight, null );

           }
        for (int i = 0,n = mBlackPieceArray.size();i < n;i++){
            Point mBlackPoint = mBlackPieceArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    ( mBlackPoint.x+(1-RATIO_PIECE_OF_LINE_HEIGHT)/2)*mLineHeight,
                    ( mBlackPoint.y+(1-RATIO_PIECE_OF_LINE_HEIGHT)/2)*mLineHeight, null );

        }

    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
          int w = mpanelWidth;
          float lineHeight = mLineHeight;
          for (int i=0;i<MAX_LINE;i++){
               int startX = (int) (lineHeight/2);
               int endX = (int) (w-(lineHeight/2));
               int y = (int) ((0.5+i)*lineHeight);
              canvas.drawLine(startX, y, endX, y, mPaint);//画横线
              canvas.drawLine(y, startX, y, endX, mPaint);//画竖线
          }

    }
    

}
