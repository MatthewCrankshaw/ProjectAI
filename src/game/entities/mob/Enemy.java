package game.entities.mob;

import game.animators.mob_animators.CharacterAnimator;
import game.entities.ability.Ability;
import game.entities.ability.ability_managers.AITeleportManager;
import game.entities.ability.ability_managers.AbilityManager;
import game.graphics.Screen;
import game.graphics.sprite.Sprite;
import game.levels.Level;
import game.levels.tile.Tile;

public class Enemy extends Mob{

    public static int ENEMY_TELEPORT_SPEED = 1200;
    private CharacterAnimator characterAnimator;
    private AITeleportManager teleportManager;
    private int movX, movY = 0;
    private int currentLife, maxLife;

    public Enemy(int x, int y, Level level, Screen screen, String name, int speed, Sprite[][] spriteName){
        super(level, screen, name, speed, 1);
        this.x  = x;
        this.y = y;
        currentLife = 100;
        maxLife = 100;
        characterAnimator = new CharacterAnimator(screen, 4, spriteName, this, 120, mobScale);
        teleportManager = new AITeleportManager(screen, this);
    }

    @Override
    public void tick() {

        for(Ability a : AbilityManager.abilityList) {
            int damage = isHit((int)a.getX(), (int)a.getY(), a.getDamage());
            if (damage != 0){
                currentLife -= damage;
                a.setExploding();
            }
        }

        if(currentLife <= 0){
            alive = false;
        }

        teleportManager.tick();

        Tile.swimming.tick();
        if (!stuck) {
        int xa = 0;
        int ya = 0;
        if (movY == 1) {
            ya--;
        }
        if (movY == -1) {
            ya++;
        }
        if (movX == -1) {
            xa--;
        }
        if (movX == 1) {
            xa++;
        }

        if (!teleportManager.isInAnimation()) {
            if (xa != 0 || ya != 0) {
                move(xa, ya);
                moving = true;
            } else {
                moving = false;
            }

            movX = 0;
            movY = 0;
        }

        }else {
            if (!teleportManager.isInAnimation()) {
                teleportManager.reset();
                teleportManager.setInAnimation(true);
            }
        }

        isStuck();
    }

    @Override
    public void render(Screen screen) {
        if (teleportManager.isInAnimation()){
            teleportManager.renderSprite(x, y);
        }else{
            characterAnimator.renderSprite((int)x,(int)y);
        }
    }

    private int isHit(int x, int y, int damage){
        if (x < this.x || x > this.x+16*mobScale) return 0;
        if (y < this.y || y > this.y+16*mobScale) return 0;
        return damage;
    }

    public int getLife() {
        return currentLife;
    }

    public void setLife(int life) {
        this.currentLife = life;
    }

    public int getMaxLife(){
        return this.maxLife;
    }

    public void setMaxLife(int life){
        this.maxLife = life;
    }

    public void moveUp(){
        movY = 1;
    }

    public void moveDown(){
        movY = -1;
    }

    public void moveLeft(){
        movX = -1;
    }

    public void moveRight(){
        movX = 1;
    }
}
