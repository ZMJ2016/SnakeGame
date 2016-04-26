package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Snake extends JPanel {
	// 设置地图的长和宽
	public static final int WIDTH = 30;
	public static final int HEIGHT = 20;

	// 设置格子的高和宽
	public static final int CELLWIDTH = 40;// 这是像素
	public static final int CELLHEIGHT = 40;// 这是像素

	// 设置地图,使用布尔类型
	private boolean[][] map = new boolean[HEIGHT][WIDTH];

	// 设置蛇的坐标，用集合LinkedList与Point类
	LinkedList<Point> link = new LinkedList<Point>();

	// 食物的设置
	Point food = null;

	// 控制游戏的结束
	static boolean gameOver = false;

	static boolean flag = false;

	// 初始化地图
	public void setMap() {

		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i == 0 || i == (HEIGHT - 1)) {
					map[i][j] = true;
				}

			}
		}

	}// end

	// 蛇的坐标初始化
	public void setSnake() {
		int x = WIDTH / 2;
		int y = HEIGHT / 2;

		link.addFirst(new Point(x - 1, y));
		link.addFirst(new Point(x, y));
		link.addFirst(new Point(x + 1, y));

	}// end

	// 食物的坐标初始化
	public void setFood() {
		Random random = new Random();
		while (true) {

			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			if (y != 0 && y != HEIGHT - 1) {
				food = new Point(x, y);
				break;
			}
		}

	}// end

	// 吃食物，蛇会长长；
	public boolean eatFood() {
		Point head = link.getFirst();
		if (head.equals(food)) {
			return true;
		}
		return false;
	}

	// 设置画笔,画地图
	public void paint(Graphics g) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]) {
					// 边界
					g.setColor(Color.ORANGE);
				} else {
					// 空地
					g.setColor(Color.WHITE);
				}
				// 画出矩形
				g.fill3DRect(j * CELLWIDTH, i * CELLHEIGHT, CELLWIDTH,
						CELLHEIGHT, true);
			}

		}

		// 画蛇的身体
		g.setColor(Color.YELLOW);
		for (int i = 1; i < link.size(); i++) {
			Point point = link.get(i);
			g.fill3DRect(point.x * CELLWIDTH, point.y * CELLHEIGHT, CELLWIDTH,
					CELLHEIGHT, true);
		}
		// 画蛇的头
		g.setColor(Color.RED);
		Point point = link.getFirst();
		/*
		 * if(map[point.y][point.x]=='*'){ gameOver = true; }
		 */
		g.fill3DRect(point.x * CELLWIDTH, point.y * CELLHEIGHT, CELLWIDTH,
				CELLHEIGHT, true);

		// 画食物
		g.setColor(Color.GREEN);
		g.fill3DRect(food.x * CELLWIDTH, food.y * CELLHEIGHT, CELLWIDTH,
				CELLHEIGHT, true);
		// 画GameOver图标

		g.setColor(Color.lightGray);
		g.setFont(new Font("宋体", Font.BOLD, 50));
		g.drawString("@author ZMJ", 470, 750);

		g.setColor(Color.YELLOW);
		g.setFont(new Font("宋体", Font.BOLD, 80));
		g.drawString("    天下第一斗图大会",80,110);
		g.drawString("         是真的真的好想你",80,250);
		g.drawString("不是假的假的好想你",30,350);
		g.drawString("     友谊的小船说翻就翻",30,500);
		g.drawString("        爱情的巨轮说沉就沉",30,630);
		if (flag) {
			g.setColor(Color.RED);
			g.setFont(new Font("宋体", Font.BOLD, 80));
			g.drawString("GAME OVER", 80, 300);
		}
	}

	// 蛇的移动，原理是加个蛇头,减个蛇尾巴
	public static final int Up = 1;
	public static final int DONW = -1;
	public static final int RIGHT = -2;
	public static final int LIEF = 2;

	public int ORIENTATION = -2;

	public void decideOrientation(int orientation) {
		if (orientation + ORIENTATION != 0) {
			this.ORIENTATION = orientation;
		}
	}

	// 蛇的移动
	public void move(final JFrame frame) {
		new Thread() {
			public void run() {
//				addKey(frame);
				while (true) {
					Point head = link.getFirst();

					switch (ORIENTATION) {
					case Up:
						/*
						 * if(head.y==0){ link.addFirst(new
						 * Point(head.x,HEIGHT-1)); }else{
						 */
						link.addFirst(new Point(head.x, head.y - 1));
						// }
						break;
					case DONW:
						/*
						 * if(head.y == HEIGHT-1){ link.addFirst(new
						 * Point(head.x,0)); }else{
						 */
						link.addFirst(new Point(head.x, head.y + 1));
						// }
						break;
					case LIEF:
						if (head.x == 0) {
							link.addFirst(new Point(WIDTH - 1, head.y));
						} else {
							link.addFirst(new Point(head.x - 1, head.y));
						}
						break;
					case RIGHT:
						if (head.x == WIDTH - 1) {
							link.addFirst(new Point(0, head.y));
						} else {
							link.addFirst(new Point(head.x + 1, head.y));
						}
						break;

					default:
						break;
					}
					// 吃食物
					if (eatFood()) {
						setFood();// 增加蛇尾巴，并且改变食物的位置
					} else {
						link.removeLast();// 删除尾部
					}

					repaint();// 刷新游戏界面
					try {
						Thread.sleep(500);

					} catch (Exception e) {
						e.printStackTrace();
					}
					setGameOver();
					if (gameOver) {
						JOptionPane.showMessageDialog(frame, "请不要迷恋哥", "MJ防沉迷智能系统",JOptionPane.WARNING_MESSAGE);
						System.exit(1);
					}

				}// while

			}
		}.start();
	}// end

	// 控制游戏结束
	public void setGameOver() {
		Point point = link.getFirst();
		// 碰墙死
		if (point.y == 0 || point.y == HEIGHT - 1) {
			gameOver = true;
		}
		// 吃蛇身死
		for (int i = 1; i < link.size(); i++) {
			Point body = link.get(i);
			if (point.equals(body)) {
				gameOver = true;
			}
		}
	}

	public void addKey(JFrame frame) {
		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {

				int code = e.getKeyCode();
				switch (code) {
				case 38:
					decideOrientation(Up);
					break;
				case 40:
					decideOrientation(DONW);
					break;
				case 37:
					decideOrientation(LIEF);
					break;
				case 39:
					decideOrientation(RIGHT);
					break;
				default:
					break;
				}
			}
		});

	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame("贪吃蛇");
		final Snake snake = new Snake();
		// 初始化地图
		snake.setMap();
		// 蛇的初始化
		snake.setSnake();
		// 食物的初始化
		snake.setFood();
		frame.add(snake);
		FrameUtil.initFrame(frame, WIDTH * CELLWIDTH + 20, HEIGHT * CELLHEIGHT
				+ 40);
		// 添加事件监听器
		snake.addKey(frame);
		snake.move(frame);
		/*
		 * frame.addKeyListener(new KeyAdapter() { public void keyPressed(final
		 * KeyEvent e) {
		 * 
		 * int code = e.getKeyCode(); switch (code) { case 38:
		 * snake.decideOrientation(Up); break; case 40:
		 * snake.decideOrientation(DONW); break; case 37:
		 * snake.decideOrientation(LIEF); break; case 39:
		 * snake.decideOrientation(RIGHT); break; default: break; }
		 * 
		 * snake.move();
		 */
		// snake.repaint();// 调用画笔重新再在画，相当于刷新界面
		/*
		 * snake.setGameOver(); if (gameOver) {
		 * JOptionPane.showMessageDialog(frame, "请不要迷恋哥", "MJ防沉迷智能系统",
		 * JOptionPane.WARNING_MESSAGE); System.exit(1); }
		 */

	}

}