package com.example.android.snake;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
public class SnakeView extends BasicView implements OnClickListener{

	private int mMode = READY;

	public static final int READY = 1;
	public static final int RUNNING = 2;
	// /////////////20160511////////////////
	public static final int PAUSE = 0;
	public static final int LOSE = 3;

	private int mDirection = NORTH;

	private int mNextDirection = NORTH;
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int EAST = 3;
	private static final int WEST = 4;

	// /////////////20160511////////////////

	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR = 2;
	private static final int GREEN_STAR = 3;
	private static final int APPLE = 4;

	private long mMoveDelay = 100;

	private long mLastMove;



	int score=0;
	// /////////////////20160510/////////////////

	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	// //////////////////////20160517//////////////////////
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();
	///////////////////////////0518//////////////////////////////////////

	private TextView mstatusText;

	public void setTextView(TextView textView)
	{
		mstatusText=textView;
	}
	///////////////////////////0518///////////////////////////////////
	private Button mStart; // 下面的五个按钮分别是我加的。因为原来的只能响应上下左右键的操作。
	//这边是满足触屏的操作效果。可以在布局文件中看相关的布局。

	private Button mLeft;

	private Button mRight;

	private Button mTop;

	private Button mBottom;

	public void setStartButton(Button button) {
		mStart = button;
		mStart.setOnClickListener(this);
	}

	private static final Random RNG = new Random();

	private class Coordinate {
		public int x;
		public int y;

		public Coordinate(int newX, int newY) {
			x = newX;
			y = newY;
		}

		public boolean equals(Coordinate other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return "Coordinate: [" + x + "," + y + "]";
		}
	}

	// /////////////////20160510/////////////////



	private MyHandler mHandler = new MyHandler();

	private String d;

	private int index;

	private Object localSharedPreferences;



	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			SnakeView.this.update();
			SnakeView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	public SnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ///////////////////////20160504//////////////////////////
		initSnakeView();
	}

	public SnakeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// ///////////////////////20160504//////////////////////////
		initSnakeView();
	}

	// ///////////////////////20160504//////////////////////////
	private void initSnakeView() {
		setFocusable(true);

		Resources r = this.getContext().getResources();

		resetPic(5);
		loadPic(RED_STAR, r.getDrawable(R.drawable.redstar));
		loadPic(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
		loadPic(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		loadPic(APPLE, r.getDrawable(R.drawable.apple));

	}

	// ///////////////////////20160504//////////////////////////

	private void initNewGame() {

		mMoveDelay = 600;

		// /////////////////20160510/////////////////
		mSnakeTrail.clear();

		mSnakeTrail.add(new Coordinate(7, 7));
		mSnakeTrail.add(new Coordinate(6, 7));
		mSnakeTrail.add(new Coordinate(5, 7));
		mSnakeTrail.add(new Coordinate(4, 7));
		mSnakeTrail.add(new Coordinate(3, 7));
		mSnakeTrail.add(new Coordinate(2, 7));
		// /////////////////20160510/////////////////

		// ////////////////20160511/////////////////////
		mNextDirection = NORTH;

		// //////////////////20160517//////////////////////
		// 添加苹果
		mAppleList.clear();
		addRandomApple();
		addRandomApple();

	}

	// ////////////////////20160517////////////////////////////
	private void addRandomApple() {
		// TODO Auto-generated method stub
		Coordinate newCoord = null;
		//
		//		int newX = 1 + RNG.nextInt(xCount - 2);
		//		int newY = 1 + RNG.nextInt(yCount - 2);
		//
		//		newCoord = new Coordinate(newX, newY);
		//
		//		mAppleList.add(newCoord);


		boolean found = false;

		///循环结束的条件是什么？
		while (!found) {

			int newX = 1 + RNG.nextInt(xCount - 2);
			int newY = 1 + RNG.nextInt(yCount - 2);
			newCoord = new Coordinate(newX, newY);

			boolean collision = false;
			int snakelength = mSnakeTrail.size();
			for (int i = 0; i < snakelength; i++) {
				if (mSnakeTrail.get(i).equals(newCoord)) {
					collision = true;
				}
			}
			//生成的苹果与原有苹果位置重合
			for(int j=0;j<mAppleList.size();j++)
			{
				if(mAppleList.get(j).equals(newCoord))
				{
					collision=true;
				}
			}
			found = !collision;
		}
		//作者写的时候没有考虑新的苹果坐标与原有的苹果坐标相重合的事

		mAppleList.add(newCoord);



	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMode == READY | mMode == LOSE) {
				initNewGame();
				setMode(RUNNING);
				update();
				return (true);
			}
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			return (true);
		}

		return super.onKeyDown(keyCode, msg);
	}

	public int getGameState() {
		return mMode;
	}

	public void setMode(int newMode) {
		int oldMode = mMode;
		mMode = newMode;

		//		if (newMode == RUNNING & oldMode != RUNNING) {
		//			update();
		//			return;
		//		}
		//		


		if (newMode == RUNNING & oldMode != RUNNING) {
			mstatusText.setVisibility(View.INVISIBLE);
			mStart.setVisibility(View.INVISIBLE);

			update();
			return;
		}
		/**
		 * 非运行状态时，文本显示内容有几种情况
		 * 1、ready状态，提示按UP键开始游戏
		 * 2、Pause状态，提示按UP键接着进行、
		 * 3、如果是Lose状态，提示失败，显示得分，提示重新开始
		 * */
		Resources res = getContext().getResources();
		CharSequence str = "";
		if (newMode == PAUSE) {
			str = "Press Start to resume";
		}
		if (newMode == READY) {
			str = "Press Start to play";
		}
		if (newMode == LOSE) {
			str ="Game over\n Score=" +score
					+ "\n Press Start to play";
		}

		mstatusText.setText(str);
		mstatusText.setVisibility(View.VISIBLE);      
		mStart.setVisibility(View.VISIBLE);

		mLeft.setVisibility(View.INVISIBLE);
		mRight.setVisibility(View.INVISIBLE);
		mTop.setVisibility(View.INVISIBLE);
		mBottom.setVisibility(View.INVISIBLE);
	}
	//设计暂停时保存
	public Bundle saveState() {
		Bundle map = new Bundle();

		map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		map.putInt("mDirection", Integer.valueOf(mDirection));
		map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
		map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		map.putLong("mScore", Long.valueOf(score));
		map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

		return map;
	}

	private int[] coordArrayListToArray(ArrayList<Coordinate> mAppleList2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Given a flattened array of ordinate pairs, we reconstitute them into a ArrayList of
	 * Coordinate objects
	 * 
	 * @param rawArray : [x1,y1,x2,y2,...]
	 * @return a ArrayList of Coordinates
	 */
	private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}

	/**
	 * Restore game state if our process is being relaunched
	 * 
	 * @param icicle a Bundle containing the game state
	 */
	public void restoreState(Bundle icicle) {
		setMode(PAUSE);

		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		mMoveDelay = icicle.getLong("mMoveDelay");
		score = (int) icicle.getLong("mScore");
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	}


	/*public Bundle saveState() {
		Bundle map = new Bundle();

		map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		map.putInt("mDirection", Integer.valueOf(mDirection));
		map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
		map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		map.putLong("Score", Long.valueOf(score));
		map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

		return map;
	}*/
	private ArrayList<SnakeView.Coordinate> coordStringToArrayList(String val) {
		ArrayList<SnakeView.Coordinate> coordArrayList = new ArrayList<SnakeView.Coordinate>();
		StringTokenizer st = new StringTokenizer(val, "|");
		if(!st.hasMoreTokens()) {
			return coordArrayList;
		}
		SnakeView.Coordinate c = new SnakeView.Coordinate(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		coordArrayList.add(c);
		return coordArrayList;
	}
	private ArrayList<int[]> stringToArrayList(String val) {
		ArrayList<int[]> coordArrayList = new ArrayList<int[]>();
		StringTokenizer st = new StringTokenizer(val, "|");
		if(!st.hasMoreTokens()) {
			return coordArrayList;
		}
		coordArrayList.add(new int[] {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())});
		return coordArrayList;
	}



	/**
	 * Restore game state if our process is being relaunched
	 * 
	 * @param icicle a Bundle containing the game state
	 */
	/*	public void restoreState(Bundle icicle) {
		setMode(PAUSE);

		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		mMoveDelay = icicle.getLong("mMoveDelay");
		score = (int) icicle.getLong("Score");
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	}*/
	//暂停的操作



	public void update() {
		if (mMode == RUNNING) {
			long now = System.currentTimeMillis();

			if (now - mLastMove > mMoveDelay) {
				clearMaps();

				// ///////////////////////20160504//////////////////////////
				updateWalls();
				// ///////////////////////20160504//////////////////////////

				updateSnake();

				// //////////////////////////20160517///////////////////////////////
				updateApple();

				mLastMove = now;
			}
			mHandler.sleep(mMoveDelay);
		}

	}

	private void updateApple() {
		// TODO Auto-generated method stub
		for (Coordinate c : mAppleList) {
			setMap(APPLE, c.x, c.y);
		}
	}

	// ///////////////////////20160504//////////////////////////
	private void updateWalls() {
		for (int x = 0; x < xCount; x++) {
			setMap(GREEN_STAR, x, 0);
			setMap(GREEN_STAR, x, yCount - 1);
		}
		for (int y = 1; y < yCount - 1; y++) {
			setMap(GREEN_STAR, 0, y);
			setMap(GREEN_STAR, xCount - 1, y);
		}
	}

	// /////////////////20160510/////////////////
	private void updateSnake() {

		boolean growSnake = false;

		// grab the snake by the head
		Coordinate head = mSnakeTrail.get(0);
		Coordinate newHead = new Coordinate(1, 1);

		// /////////////20160511鏇存柊铔囩殑杩愯鏂瑰悜//////////////////
		mDirection = mNextDirection;

		switch (mDirection) {
		case EAST: {
			newHead = new Coordinate(head.x + 1, head.y);
			break;
		}
		case WEST: {
			newHead = new Coordinate(head.x - 1, head.y);
			break;
		}
		case NORTH: {
			newHead = new Coordinate(head.x, head.y - 1);
			break;
		}
		case SOUTH: {
			newHead = new Coordinate(head.x, head.y + 1);
			break;
		}
		}

		// //////////////////////////////////////////////////
		// newHead = new Coordinate(head.x + 1, head.y);

		mSnakeTrail.add(0, newHead);



		////////////////20160517//////////////////////



		int applecount = mAppleList.size();
		for (int i = 0; i < applecount; i++) {
			Coordinate c = mAppleList.get(i);
			if (c.equals(newHead)) {
				mAppleList.remove(c);
				addRandomApple();
				growSnake = true;
				score++;
				mMoveDelay=(long) (mMoveDelay*0.98);
			}
		}

		if (!growSnake) {
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}

		// /////20160511//////////////////
		if (newHead.x < 1 | newHead.x > xCount - 2 | newHead.y < 1
				| newHead.y > yCount - 2) {
			setMode(LOSE);
		}

		// /////20160511鍒ゆ柇铔囨槸鍚︽挒鍒拌嚜宸�//////////////////
		int length = mSnakeTrail.size();
		for (int i = 1; i < length; i++) {
			Coordinate c = mSnakeTrail.get(i);
			if (c.equals(newHead)) {
				setMode(LOSE);
			}
		}

		int index = 0;
		for (Coordinate c : mSnakeTrail) {
			if (index == 0) {
				setMap(YELLOW_STAR, c.x, c.y);
			} else {
				setMap(RED_STAR, c.x, c.y);
			}
			index++;
		}

	}
	// /////////////////20160510/////////////////
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.start:
			if(mMode == READY | mMode == LOSE){
				initNewGame();
				setMode(RUNNING);
				update();
				mStart.setVisibility(View.GONE);
				mLeft.setVisibility(View.VISIBLE);
				mRight.setVisibility(View.VISIBLE);
				mTop.setVisibility(View.VISIBLE);
				mBottom.setVisibility(View.VISIBLE);
			}
			if (mMode == PAUSE) {
				setMode(RUNNING);
				update();
				mStart.setVisibility(View.GONE);
				mLeft.setVisibility(View.VISIBLE);
				mRight.setVisibility(View.VISIBLE);
				mTop.setVisibility(View.VISIBLE);
				mBottom.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.left:
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			break;
		case R.id.right:
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			break;
		case R.id.top:
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			break;
		case R.id.bottom:
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			break;
		default:
			break;

		}				
	}

	// 设置方向键
	public void setControlButton(Button left, Button right, Button top, Button bottom) {
		mLeft = left;
		mRight = right;
		mTop = top;
		mBottom = bottom;
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mTop.setOnClickListener(this);
		mBottom.setOnClickListener(this);
	}
	/*//OntouchEveant 就是设置屏幕点击事件
	 * 首先就是获取舌头的位置坐标  Coordinate head = mSnakeTrail.get(0);
	 * 在就是获取屏幕的点击的位置坐标
	 */

	private int checkSnakeDirecttion(float x, float y){

		if(mSnakeTrail.size()>0){
			SnakeView.Coordinate head=(SnakeView.Coordinate)mSnakeTrail.get(0);
			SnakeView.Coordinate head1=(SnakeView.Coordinate)mSnakeTrail.get(1);
			int [] point=checkTouchPoint(x,y);
			if(head.x==head1.x){
				if(point[0x0]>head.x){
					return WEST;
				}
				return EAST;
			}
			if(head.y==head1.y){
				if(point[0x1]>head.y){
					return SOUTH;
				}
			}
		}

		return NORTH;

	}




	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if(event.getAction()!=0){
			return true;
		}
		if(mMode==PAUSE){
			setMode(RUNNING);
			update();
			return true;
		}/*else if(((mMode != READY ? READY : PAUSE | mMode == LOSE ? READY : mMode == LOSE ? false)!= 0)){
			initNewGame();
			setMode(0x2);
			update();
			return true;
		}*/
		int x = (int)event.getX();
		int y = (int)event.getY();
		int dire = checkSnakeDirecttion(event.getX(), event.getY());
		if(dire == 0x1) {
			if(mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return true;
		}
		if(dire == SOUTH) {
			if(mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			return true;
		}
		if(dire == EAST) {
			if(mDirection != EAST) {
				mNextDirection = WEST;
			}
			return true;
		}
		if(dire == WEST) {
			if(mDirection != WEST) {
				mNextDirection = EAST;
			}
			return true;
		}
		return super.onTouchEvent(event);
	}













}
