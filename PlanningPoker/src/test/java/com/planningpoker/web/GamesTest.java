package com.planningpoker.web;

import com.planningpoker.model.Game;
import com.planningpoker.model.Player;
import com.planningpoker.model.observer.Observer;
import com.planningpoker.web.socket.WebSocketObserver;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GamesTest {

    @Test
    public void testFindGameByPredicate() {
        // Setup
        Session session1 = mock(Session.class);
        WebSocketObserver observer1 = new WebSocketObserver(session1);

        Session session2 = mock(Session.class);
        WebSocketObserver observer2 = new WebSocketObserver(session2);

        String managerName = "Manager";

        Game game1 = Games.nextGame(managerName, observer1);
        Game game2 = Games.nextGame(managerName, observer2);

        // Test finding game 1 by session 1
        Game foundGame1 = Games.findGame(g -> {
            for(Player p : g.getPlayers()) {
                 Observer obs = p.getObserver();
                 if (obs instanceof WebSocketObserver && ((WebSocketObserver)obs).getSession().equals(session1)) {
                     return true;
                 }
            }
            return false;
        });

        assertNotNull(foundGame1);
        assertEquals(game1.getId(), foundGame1.getId());

        // Test finding game 2 by session 2
        Game foundGame2 = Games.findGame(g -> {
            for(Player p : g.getPlayers()) {
                 Observer obs = p.getObserver();
                 if (obs instanceof WebSocketObserver && ((WebSocketObserver)obs).getSession().equals(session2)) {
                     return true;
                 }
            }
            return false;
        });

        assertNotNull(foundGame2);
        assertEquals(game2.getId(), foundGame2.getId());

        // Test finding non-existent session
        Session session3 = mock(Session.class);
        Game foundGame3 = Games.findGame(g -> {
            for(Player p : g.getPlayers()) {
                 Observer obs = p.getObserver();
                 if (obs instanceof WebSocketObserver && ((WebSocketObserver)obs).getSession().equals(session3)) {
                     return true;
                 }
            }
            return false;
        });

        assertNull(foundGame3);
    }
}
