package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;


public class Test1 {
	private Board b;
	
	@Before
	public void init() {
		b=new Board(); 
	}
	
	@Test
	public void testchekifcellswinner_Player1() {
				int[] posPlayer1 = { 0, 3, 6 };
				int[] posPlayer2 = { 2, 5, 7 };
				
				for(int i=0;i<posPlayer1.length;i++) {
					b.getCell(posPlayer1[i]).value = "X";
					b.getCell(posPlayer2[i]).value = "O";
				}
				assertThat(b.getCellsIfWinner("X")).isEqualTo(posPlayer1);
	}
	
	@Test
	public void testchekifcellswinner_Player2() {
				int[] posPlayer1 = { 1, 2, 7 };
				int[] posPlayer2 = { 0, 4, 8 };
				
				for(int i=0;i<posPlayer1.length;i++) {
					b.getCell(posPlayer1[i]).value = "X";
					b.getCell(posPlayer2[i]).value = "O";
				}
				assertThat(b.getCellsIfWinner("O")).isEqualTo(posPlayer2);
	}
	
	@Test
	public void testDraw() {
		int[] posPlayer1 = { 4, 7, 2, 3, 8};
		int[] posPlayer2 = { 5, 1, 6, 0};
		
		for(int i=0;i<posPlayer1.length;i++) {
			b.getCell(posPlayer1[i]).value = "X";
			if(i!=4) {
			b.getCell(posPlayer2[i]).value = "O";
			}
		}
		assertThat(b.checkDraw()).isEqualTo(true); 
	}
	
}