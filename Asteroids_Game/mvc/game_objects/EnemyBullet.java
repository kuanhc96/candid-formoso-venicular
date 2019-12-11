package _08final.mvc.model;

public class EnemyBullet extends Bullet {
    public EnemyBullet(Sprite enemy) {
        super(enemy);
        setTeam(Team.FOE);
    }



}
