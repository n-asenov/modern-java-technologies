package bg.sofia.uni.fmi.mjt.dungeon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Enemy;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Treasure;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;

public class GameEngineTest {
    private Hero hero;
    private char[][] map;
    private Enemy[] enemies;
    private Treasure[] treasures;
    private GameEngine gameEngine;

    @Before
    public void setup() {
        hero = new Hero("hero", 100, 100);
        map = new char[][] { "#T#".toCharArray(), "TS.".toCharArray(), "#EG".toCharArray() };
        enemies = new Enemy[] { new Enemy("enemy", 100, 0, new Weapon("enemy weapon", 30), null) };
        treasures = new Treasure[] { new Weapon("strong weapon", 50),
                new Spell("fireball", 60, 10) };
        gameEngine = new GameEngine(map, hero, enemies, treasures);
    }

    @Test
    public void testMoveToEmptyBlock() {
        String moveMessage = gameEngine.makeMove(Direction.RIGHT);

        assertEquals("You moved successfully to the next position.", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToObstacle() {
        gameEngine.makeMove(Direction.RIGHT);
        String moveMessage = gameEngine.makeMove(Direction.UP);

        assertEquals("Wrong move. There is an obstacle and you cannot bypass it.", moveMessage);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToTreasure() {
        String moveMessage = gameEngine.makeMove(Direction.LEFT);

        assertEquals("Weapon found! Damage points: 50", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][0]);

        assertEquals("strong weapon", gameEngine.getHero().getWeapon().getName());
        assertEquals(50, gameEngine.getHero().getWeapon().getDamage());
    }

    @Test
    public void testMoveToEnemyWithNoAttack() {
        String moveMessage = gameEngine.makeMove(Direction.DOWN);

        assertEquals("Hero is dead! Game over!", moveMessage);
    }

    @Test
    public void testMoveToEnemyWithWeapon() {
        gameEngine.makeMove(Direction.LEFT);
        gameEngine.makeMove(Direction.RIGHT);
        String moveMessage = gameEngine.makeMove(Direction.DOWN);

        assertEquals("Enemy died.", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[2][1]);

        assertEquals(70, gameEngine.getHero().getHealth());
    }

    @Test
    public void testMoveWithWrongDirection() {
        String moveMessage = gameEngine.makeMove(null);

        assertEquals("Unknown command entered.", moveMessage);
    }

    @Test
    public void testMoveToDungeonExit() {
        gameEngine.makeMove(Direction.RIGHT);
        String moveMessage = gameEngine.makeMove(Direction.DOWN);

        assertEquals("You have successfully passed through the dungeon. Congrats!", moveMessage);
    }

    @Test
    public void testMoveToEnemyWithSpellAttack() {
        gameEngine.makeMove(Direction.LEFT);
        gameEngine.makeMove(Direction.RIGHT);
        gameEngine.makeMove(Direction.UP);
        gameEngine.makeMove(Direction.DOWN);
        String moveMessage = gameEngine.makeMove(Direction.DOWN);

        assertEquals("Enemy died.", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[2][1]);
        
        assertEquals(70, gameEngine.getHero().getHealth());
        assertEquals(80, gameEngine.getHero().getMana());
    }

}