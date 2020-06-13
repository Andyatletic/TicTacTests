package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;



public class TicTacToeGameTest2 {

//Hago solo un test para ahorrar codigo, y paso los movimientos como parametros	
	@ParameterizedTest
	@MethodSource("values")
	public void TicTacToeGame_TestGana1(int argument) {
		TicTacToeGame game = new TicTacToeGame();
		Connection c0 = mock(Connection.class);
		Connection c1 = mock(Connection.class);	
		game.addConnection(c0);
		game.addConnection(c1);
		ArrayList<Player> jugadores = new ArrayList<Player>();
		jugadores.add(new Player(0, "Pepe", "X"));
				
		//añadimos el primer jugador a la partida y verificamos que se envian los mensajes
		game.addPlayer(jugadores.get(0));
		verify(c0).sendEvent(eq(EventType.JOIN_GAME), eq(jugadores));
		verify(c1).sendEvent(eq(EventType.JOIN_GAME), eq(jugadores));
		
		//añadimos otro jugador a la lista de jugadores, lo añadimos a la partida y verificamos
		jugadores.add(new Player(1, "Juan", "O"));
		game.addPlayer(jugadores.get(1));
		verify(c0, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(jugadores));
		verify(c1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(jugadores));	
		//verifica que se pasa de turno
		verify(c0).sendEvent(eq(EventType.SET_TURN), eq(jugadores.get(0)));
		verify(c1).sendEvent(eq(EventType.SET_TURN), eq(jugadores.get(0)));
		ArrayList<Integer> mov1= new ArrayList<Integer>(argument);
	
		int siguienteTurno = 1;
		//Recorremos los movimientos que se producen
		for (int i=0;i<mov1.size();i++) {
			int TurnoActual = i;
			game.mark(mov1.get(i));
			ArgumentCaptor<CellMarkedValue> argumentoCelda = ArgumentCaptor.forClass(CellMarkedValue.class);
			verify(c0).sendEvent(eq(EventType.MARK), argumentoCelda.capture());
			assertThat(argumentoCelda.getValue().cellId).isEqualTo(mov1.get(i));
			assertThat(argumentoCelda.getValue().player.getId()).isEqualTo(TurnoActual);
			verify(c1).sendEvent(eq(EventType.MARK), argumentoCelda.capture());
			assertThat(argumentoCelda.getValue().cellId).isEqualTo(mov1.get(i) );
			assertThat(argumentoCelda.getValue().player.getId()).isEqualTo(TurnoActual);
			
			if (game.checkWinner()!=null) {
				ArgumentCaptor<WinnerValue> argument1 = ArgumentCaptor.forClass(WinnerValue.class);
				verify(c0).sendEvent(eq(EventType.GAME_OVER), argument1.capture());
				assertThat(argument1.getValue().pos).isNotEqualTo(null);
				assertThat(argument1.getValue().player.getId()).isEqualTo(TurnoActual);
				verify(c1).sendEvent(eq(EventType.GAME_OVER), argument1.capture());
				assertThat(argument1.getValue().pos).isNotEqualTo(null);
				assertThat(argument1.getValue().player.getId()).isEqualTo(TurnoActual);
			}
			else if (game.checkDraw()) {
				ArgumentCaptor<WinnerValue> argument1 = ArgumentCaptor.forClass(WinnerValue.class);
				verify(c0).sendEvent(eq(EventType.GAME_OVER), argument1.capture());
				assertThat(argument1.getValue()).isNull();
				verify(c1).sendEvent(eq(EventType.GAME_OVER), argument1.capture());
				assertThat(argument1.getValue()).isNull();
			}
			else {
				verify(c0).sendEvent(eq(EventType.SET_TURN), eq(jugadores.get(siguienteTurno)));
				verify(c1).sendEvent(eq(EventType.SET_TURN), eq(jugadores.get(siguienteTurno)));
			}
			siguienteTurno =siguienteTurno+1;
			reset(c0);
			reset(c1);
		}
				
		
	}
	public static Collection<Object[]> values() {
		 Object[][] values = {
		//Gana jugador 1
		 {2,5,4,1,6},
		 //Gana jugador 2
		 {0,4,3,6,1,2},
		 //Empate
		 {2,0,3,1,4,5,7,6,8}
		 };
		 return Arrays.asList(values);
		 } 
}