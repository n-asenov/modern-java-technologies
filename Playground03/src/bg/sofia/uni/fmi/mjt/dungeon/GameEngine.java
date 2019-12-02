package bg.sofia.uni.fmi.mjt.dungeon;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Enemy;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Position;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Treasure;

public class GameEngine {
    private static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command entered.";
    private static final String SUCCESSFUL_MOVE_MESSAGE = "You moved successfully to the next position.";
    private static final String WRONG_MOVE_MESSAGE = "Wrong move. There is an obstacle and you cannot bypass it.";
    private static final String ENEMY_DIED_MESSAGE = "Enemy died.";
    private static final String GAME_OVER_MESSAGE = "Hero is dead! Game over!";
    private static final String WIN_MESSAGE = "You have successfully passed through the dungeon. Congrats!";

    private char[][] map;
    private Hero hero;
    private Position heroPositon;
    private Enemy[] enemies;
    private int currentEnemy;
    private Treasure[] treasures;
    private int currentTreasure;

    public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures) {
        this.map = map;
        this.hero = new Hero(hero.getName(), hero.getHealth(), hero.getMana());
        this.hero.equip(hero.getWeapon());
        this.hero.learn(hero.getSpell());
        this.enemies = enemies;
        this.treasures = treasures;
        heroPositon = findHeroStartPositon();
        currentEnemy = 0;
        currentTreasure = 0;
    }

    public char[][] getMap() {
        return map;
    }

    public Hero getHero() {
        return hero;
    }

    public Position getHeroPosition() {
        return heroPositon;
    }

    public String makeMove(Direction direction) {
        if (direction == null) {
            return UNKNOWN_COMMAND_MESSAGE;
        }

        Position nextPosition = getNextHeroPositon(direction);

        if (!isValidPosition(nextPosition)) {
            return WRONG_MOVE_MESSAGE;
        }

        if (isEmptyPosition(nextPosition)) {
            moveHero(nextPosition);
            return SUCCESSFUL_MOVE_MESSAGE;
        }

        if (hasTreasure(nextPosition)) {
            moveHero(nextPosition);
            return treasures[currentTreasure++].collect(hero);
        }

        if (hasEnemy(nextPosition)) {
            if (battle(enemies[currentEnemy++])) {
                moveHero(nextPosition);
                return ENEMY_DIED_MESSAGE;
            }
            
            return GAME_OVER_MESSAGE;
        }

        return WIN_MESSAGE;
    }

    private Position findHeroStartPositon() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'S') {
                    return new Position(row, col);
                }
            }
        }

        return null;
    }

    private Position getNextHeroPositon(Direction direction) {
        int xHeroPosition = heroPositon.getX();
        int yHeroPosition = heroPositon.getY();

        if (direction == Direction.DOWN) {
            return new Position(xHeroPosition + 1, yHeroPosition);
        }

        if (direction == Direction.UP) {
            return new Position(xHeroPosition - 1, yHeroPosition);
        }

        if (direction == Direction.LEFT) {
            return new Position(xHeroPosition, yHeroPosition - 1);
        }

        return new Position(xHeroPosition, yHeroPosition + 1);
    }

    private boolean isValidPosition(Position position) {
        int x = position.getX();
        int y = position.getY();

        return x >= 0 && x < map.length && y >= 0 && y < map[x].length && map[x][y] != '#';
    }

    private boolean isEmptyPosition(Position position) {
        return map[position.getX()][position.getY()] == '.';
    }

    private boolean hasTreasure(Position position) {
        return map[position.getX()][position.getY()] == 'T';
    }

    private boolean hasEnemy(Position position) {
        return map[position.getX()][position.getY()] == 'E';
    }

    private void moveHero(Position nextPosition) {
        map[heroPositon.getX()][heroPositon.getY()] = '.';
        map[nextPosition.getX()][nextPosition.getY()] = 'H';
        heroPositon = nextPosition;
    }

    private boolean battle(Enemy enemy) {
        boolean heroTurn = true;

        while (hero.isAlive() && enemy.isAlive()) {
            if (heroTurn) {
                enemy.takeDamage(hero.attack());
                heroTurn = false;
            } else {
                hero.takeDamage(enemy.attack());
                heroTurn = true;
            }
        }

        if (enemy.isAlive()) {
            return false;
        }

        return true;
    }
}
