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
	// ���õ�ͼ�ĳ��Ϳ�
	public static final int WIDTH = 30;
	public static final int HEIGHT = 20;

	// ���ø��ӵĸߺͿ�
	public static final int CELLWIDTH = 40;// ��������
	public static final int CELLHEIGHT = 40;// ��������

	// ���õ�ͼ,ʹ�ò�������
	private boolean[][] map = new boolean[HEIGHT][WIDTH];

	// �����ߵ����꣬�ü���LinkedList��Point��
	LinkedList<Point> link = new LinkedList<Point>();

	// ʳ�������
	Point food = null;

	// ������Ϸ�Ľ���
	static boolean gameOver = false;

	static boolean flag = false;

	// ��ʼ����ͼ
	public void setMap() {

		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i == 0 || i == (HEIGHT - 1)) {
					map[i][j] = true;
				}

			}
		}

	}// end

	// �ߵ������ʼ��
	public void setSnake() {
		int x = WIDTH / 2;
		int y = HEIGHT / 2;

		link.addFirst(new Point(x - 1, y));
		link.addFirst(new Point(x, y));
		link.addFirst(new Point(x + 1, y));

	}// end

	// ʳ��������ʼ��
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

	// ��ʳ��߻᳤����
	public boolean eatFood() {
		Point head = link.getFirst();
		if (head.equals(food)) {
			return true;
		}
		return false;
	}

	// ���û���,����ͼ
	public void paint(Graphics g) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]) {
					// �߽�
					g.setColor(Color.ORANGE);
				} else {
					// �յ�
					g.setColor(Color.WHITE);
				}
				// ��������
				g.fill3DRect(j * CELLWIDTH, i * CELLHEIGHT, CELLWIDTH,
						CELLHEIGHT, true);
			}

		}

		// ���ߵ�����
		g.setColor(Color.YELLOW);
		for (int i = 1; i < link.size(); i++) {
			Point point = link.get(i);
			g.fill3DRect(point.x * CELLWIDTH, point.y * CELLHEIGHT, CELLWIDTH,
					CELLHEIGHT, true);
		}
		// ���ߵ�ͷ
		g.setColor(Color.RED);
		Point point = link.getFirst();
		/*
		 * if(map[point.y][point.x]=='*'){ gameOver = true; }
		 */
		g.fill3DRect(point.x * CELLWIDTH, point.y * CELLHEIGHT, CELLWIDTH,
				CELLHEIGHT, true);

		// ��ʳ��
		g.setColor(Color.GREEN);
		g.fill3DRect(food.x * CELLWIDTH, food.y * CELLHEIGHT, CELLWIDTH,
				CELLHEIGHT, true);
		// ��GameOverͼ��

		g.setColor(Color.lightGray);
		g.setFont(new Font("����", Font.BOLD, 50));
		g.drawString("@author ZMJ", 470, 750);

		g.setColor(Color.YELLOW);
		g.setFont(new Font("����", Font.BOLD, 80));
		g.drawString("    ���µ�һ��ͼ���",80,110);
		g.drawString("         �������ĺ�����",80,250);
		g.drawString("���Ǽٵļٵĺ�����",30,350);
		g.drawString("     �����С��˵���ͷ�",30,500);
		g.drawString("        ����ľ���˵���ͳ�",30,630);
		if (flag) {
			g.setColor(Color.RED);
			g.setFont(new Font("����", Font.BOLD, 80));
			g.drawString("GAME OVER", 80, 300);
		}
	}

	// �ߵ��ƶ���ԭ���ǼӸ���ͷ,������β��
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

	// �ߵ��ƶ�
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
					// ��ʳ��
					if (eatFood()) {
						setFood();// ������β�ͣ����Ҹı�ʳ���λ��
					} else {
						link.removeLast();// ɾ��β��
					}

					repaint();// ˢ����Ϸ����
					try {
						Thread.sleep(500);

					} catch (Exception e) {
						e.printStackTrace();
					}
					setGameOver();
					if (gameOver) {
						JOptionPane.showMessageDialog(frame, "�벻Ҫ������", "MJ����������ϵͳ",JOptionPane.WARNING_MESSAGE);
						System.exit(1);
					}

				}// while

			}
		}.start();
	}// end

	// ������Ϸ����
	public void setGameOver() {
		Point point = link.getFirst();
		// ��ǽ��
		if (point.y == 0 || point.y == HEIGHT - 1) {
			gameOver = true;
		}
		// ��������
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
		final JFrame frame = new JFrame("̰����");
		final Snake snake = new Snake();
		// ��ʼ����ͼ
		snake.setMap();
		// �ߵĳ�ʼ��
		snake.setSnake();
		// ʳ��ĳ�ʼ��
		snake.setFood();
		frame.add(snake);
		FrameUtil.initFrame(frame, WIDTH * CELLWIDTH + 20, HEIGHT * CELLHEIGHT
				+ 40);
		// ����¼�������
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
		// snake.repaint();// ���û����������ڻ����൱��ˢ�½���
		/*
		 * snake.setGameOver(); if (gameOver) {
		 * JOptionPane.showMessageDialog(frame, "�벻Ҫ������", "MJ����������ϵͳ",
		 * JOptionPane.WARNING_MESSAGE); System.exit(1); }
		 */

	}

}