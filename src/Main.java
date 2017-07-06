
public class Main {
	public static void main(String[] args) {
		Maze maze = new Maze(100);
		
		maze.generator(0, 0);
		
		maze.solver(0, 0);
		
		/*
		 * In the situation when there is a solution for this maze
		 * Both:
		 * - white dot for start
		 * - green dot for end
		 * - blue dots for route
		 * will occur on the display
		 * Otherwise only white dot will occur
		 * */
		
		maze.display();
		
	}
}
