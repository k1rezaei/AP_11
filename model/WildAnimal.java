
public class WildAnimal extends Animal {
    void collide(Entity entity) {
        if(entity instanceof FarmAnimal) {
            entity.destroy();
        }
        return ;
    }
}
