import java.awt.Color;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;

class Cell {
	private int x;
	private int y;
	private boolean[] walls = new boolean[4]; //North, West, East, South
	private int color; 
	/*
	* 0 - WHITE, 1- GREY, 2 - BLACK
	* FOR SOLVER: 3 - WHITE, 4 - START CELL 5 - FINAL CELL 6 = ROUTE
	*/
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.walls[0] = this.walls[1] = this.walls[2] = this.walls[3] = true;
		this.color = 0;
	}
	@Override
	public String toString() {
		return "Cell [x=" + x + ", y=" + y + ", walls=" + Arrays.toString(walls) + ", color=" + color + "]";
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean[] getWalls() {
		return walls;
	}
	public void setWalls(boolean[] walls) {
		this.walls = walls;
	}
	public boolean getWallAt(int i) {
		return walls[i];
	}
	public void setWallAt(int i, boolean state) {
		this.walls[i] = state;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
}

public class Maze {
	
	public int counter = 0; //Used to make sure that final cell is not to early available
	private int dimension;
	private Cell[] cells;
	
	public Maze(int dimension) {
		this.setDimension(dimension);
		cells = new Cell[dimension*dimension];
		
		for(int i = 0; i < cells.length; i++) {
			int row = i/dimension;
			int column = i%dimension;
			cells[i] = new Cell(column, row);
		}
	}
	
	public int getDimension() {
		return dimension;
	}
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public boolean findExit(Cell cell) {
		if(cell.getX() == 0) {
			cell.setWallAt(1, false);
			return true;
		}
		else if(cell.getX() == dimension-1) {
			cell.setWallAt(2, false);
			return true;
		}
		else if(cell.getY() == 0) {
			cell.setWallAt(0, false);
			return true;
		}
		else if(cell.getY() == dimension-1) {
			cell.setWallAt(3, false);
			return true;
		}
		else
			return false;
	}
	
	/*
	 * DFS
	 */
	
	public void generator(int startX, int startY) {
		Cell startCell = cells[startY*dimension + startX]; 
		findExit(startCell);
		DFS_Visit(startCell);
	}
	
	boolean EXIT = false; //To make no more than one EXIT
	private void DFS_Visit(Cell startCell) {
		counter++;
		startCell.setColor(1);
		List<Cell> neighbors = getNeighbor(startCell);
		if(neighbors.isEmpty()) {
			startCell.setColor(2);
			if(counter > dimension*(dimension/2) && EXIT == false)
				EXIT = findExit(startCell);
			return;
		}
		Collections.shuffle(neighbors);
		
		for(Cell cell : neighbors)
			if(cell.getColor() == 0) {
				wallRemover(startCell, cell);
				DFS_Visit(cell);
			}
		startCell.setColor(2);
	}
	
	private void wallRemover(Cell startCell, Cell cell) {
		int startX = startCell.getX();
		int startY = startCell.getY();
		int x = cell.getX();
		int y = cell.getY();
		if(startX == x && startY > y) {
			startCell.setWallAt(0, false);
			cell.setWallAt(3, false);
		} else if(startX == x && startY < y) {
			startCell.setWallAt(3, false);
			cell.setWallAt(0, false);
		} else if(startX > x && startY == y) {
			startCell.setWallAt(1, false);
			cell.setWallAt(2, false);
		} else if(startX < x && startY == y) {
			startCell.setWallAt(2, false);
			cell.setWallAt(1, false);
		}
	}
	
	private List<Cell> getNeighbor(Cell cell) {
		List<Cell> neighbors = new ArrayList<Cell>();
		int x = cell.getX();
		int y = cell.getY();
		if(x < dimension-1)
			if(cells[y*dimension + (x+1)].getColor() == 0)
				neighbors.add(cells[y*dimension + x + 1]);
		if(x > 0)
			if(cells[y*dimension + (x-1)].getColor() == 0)
				neighbors.add(cells[y*dimension + x - 1]);
		if(y < dimension-1)
			if(cells[(y+1)*dimension + x].getColor() == 0)
				neighbors.add(cells[(y+1)*dimension + x]);
		if(y > 0)
			if(cells[(y-1)*dimension + x].getColor() == 0)
				neighbors.add(cells[(y-1)*dimension + x]);
		return neighbors;
	}
		
	/*
	 * SOLVER
	 */
	

	boolean EXITFOUND = false;
	Cell EntranceCell;
	
	public void solver(int startX, int startY) {
		Cell startCell = cells[startY*dimension + startX];
		EntranceCell = startCell;

		solverDFS_Visit(startCell);
	}
	
	private void solverDFS_Visit(Cell startCell) {
		
		startCell.setColor(3);
		List<Cell> neighbors = getSolverNeighbor(startCell);
		
		if(neighbors.isEmpty()) {
			if(checkIfEnd(startCell)) {
				startCell.setColor(5);
				EXITFOUND = true;
				return;
			}
		}
		
		for(Cell cell : neighbors) {
			if(cell.getColor() == 2) {
				solverDFS_Visit(cell);
			}
			if(EXITFOUND == true) {
				startCell.setColor(6);
				return;
			}
		}
		
		if(EXITFOUND == true) {
			startCell.setColor(6);
		}
	}
	
	private List<Cell> getSolverNeighbor(Cell cell) {
		
		List<Cell> neighbors = new ArrayList<Cell>();
		int x = cell.getX();
		int y = cell.getY();
		
		if(x < dimension-1)
			if(cells[y*dimension + (x+1)].getColor() == 2 && cells[y*dimension + (x+1)].getWallAt(1) == false)
				neighbors.add(cells[y*dimension + x + 1]);
		if(x > 0)
			if(cells[y*dimension + (x-1)].getColor() == 2 && cells[y*dimension + (x-1)].getWallAt(2) == false)
				neighbors.add(cells[y*dimension + x - 1]);
		if(y < dimension-1)
			if(cells[(y+1)*dimension + x].getColor() == 2 && cells[(y+1)*dimension + x].getWallAt(0) == false)
				neighbors.add(cells[(y+1)*dimension + x]);
		if(y > 0)
			if(cells[(y-1)*dimension + x].getColor() == 2 && cells[(y-1)*dimension + x].getWallAt(3) == false)
				neighbors.add(cells[(y-1)*dimension + x]);	
		
		return neighbors;
	}
	
	private boolean checkIfEnd(Cell cell) {
		if(cell.getX() == 0 && cell.getWallAt(1) == false)
			return true;
		else if(cell.getX() == dimension-1 && cell.getWallAt(2) ==  false)
			return true;
		else if(cell.getY() == 0 && cell.getWallAt(0) == false)
			return true;
		else if(cell.getY() == dimension-1 && cell.getWallAt(3) == false)
			return true;
		else
			return false;
	}
	
	/*
	 * DISPLAY
	 */

	public void display() {		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Maze generator");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Panel panel = new Panel();
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);			
			}
		});
	}
	
	class Panel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int size = 1000;
		final int sizeOfCell = size/dimension;
		final int halfCell = sizeOfCell/2;
		final int eighth = halfCell/4;
		
		public Panel() {
			setBackground(Color.BLACK);
			setSize(new Dimension(size+sizeOfCell, size+sizeOfCell));
			setPreferredSize(new Dimension(size+sizeOfCell, size+sizeOfCell));
			Panel.this.repaint();
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.YELLOW);
			EntranceCell.setColor(4);
			for(int i = 0; i < cells.length; i++) {
				g.setColor(Color.YELLOW);
				int y = i/dimension;
				int x = i%dimension;
				
				//paint grid
				
				if(cells[i].getWallAt(0) == true)
					g.drawLine(x*sizeOfCell + halfCell, y*sizeOfCell + halfCell, (x+1)*sizeOfCell + halfCell, y*sizeOfCell + halfCell);
				if(cells[i].getWallAt(1) == true)
					g.drawLine(x*sizeOfCell + halfCell, y*sizeOfCell + halfCell, x*sizeOfCell + halfCell, (y+1)*sizeOfCell + halfCell);
				if(cells[i].getWallAt(2) == true)
					g.drawLine((x+1)*sizeOfCell + halfCell, y*sizeOfCell + halfCell, (x+1)*sizeOfCell + halfCell, (y+1)*sizeOfCell + halfCell);
				if(cells[i].getWallAt(3) == true)
					g.drawLine(x*sizeOfCell + halfCell, (y+1)*sizeOfCell + halfCell, (x+1)*sizeOfCell + halfCell, (y+1)*sizeOfCell + halfCell);
				
				//Paint route
				
				if(cells[i].getColor() == 4) {
					g.setColor(Color.WHITE);
					g.fillOval(x*sizeOfCell + 5*eighth, y*sizeOfCell + 5*eighth, 6*eighth, 6*eighth);
				} else if(cells[i].getColor() == 5) {
					g.setColor(Color.GREEN);
					g.fillOval(x*sizeOfCell + 5*eighth, y*sizeOfCell + 5*eighth, 6*eighth, 6*eighth);
				} else if(cells[i].getColor() == 6) {
					g.setColor(Color.BLUE);
					g.fillOval(x*sizeOfCell + 6*eighth, y*sizeOfCell + 6*eighth, 5*eighth, 5*eighth);
				}
			}		
		}
	}
}
