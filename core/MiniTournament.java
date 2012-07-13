package core;
import util.*;
import hguo1339.*;
import info1103.*;
import java.util.Collection;
import java.util.ArrayList;

public class MiniTournament {
	public static void main(String[] args) {
		Player player0 = new Hunter();
		Player player1 = new SequentialShooter();
		Player[] players = {player0, player1};
		Collection<Ship> ships = new ArrayList<Ship>();
		Ship s31 = new Ship('A', 1, 3); //will the order of the ships be given? If not, think about an algorithm for using size information
		Ship s5 = new Ship('B', 1, 5);
		Ship s32 = new Ship('C', 1, 3);
		Ship s4 = new Ship('D', 1, 4);
		Ship s2 = new Ship('E', 1, 2);
		ships.add(s2);
		ships.add(s31);
		ships.add(s32);
		ships.add(s4);
		ships.add(s5);
		BattleShips b = new BattleShips(10,10,players,ships);
		b.run();
	}
}
