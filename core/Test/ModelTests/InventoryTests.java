package ModelTests;

import com.pottda.game.Model.AttackItem;
import com.pottda.game.Model.Inventory;
import com.pottda.game.Model.SupportItem;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gustav Lahti on 2017-04-16.
 */
public class InventoryTests {
    private Inventory inventory;
    private Random random = new Random();

    @BeforeClass
    public void initInventory(){
        inventory = new Inventory(null, null, null);
    }

    @After
    public void resetInventory(){
        inventory.attackItems = null;
        inventory.supportItems = null;
        inventory.inactiveItems = null;
    }

    @Test
    public void testGetOffensive(){
        int damageInput = 0;
        int damageOutput;
        int damageTemp;

        int cooldownInput = 0;
        int cooldownOutput;
        int cooldownTemp;

        int projectileAmountInput = 1;
        int projectileAmountOutput;
        int projectileAmountTemp;

        List<AttackItem> attackItems = new ArrayList();

        for(int i = 0; i < 100; i++){
            damageTemp = random.nextInt(10);
            cooldownTemp = random.nextInt(10);
            projectileAmountTemp = random.nextInt(4);
            attackItems.add(new AttackItem(damageTemp, cooldownTemp, projectileAmountTemp, null));
            damageInput += damageTemp;
            cooldownInput += cooldownTemp;
            projectileAmountInput += projectileAmountTemp;
        }

        inventory.attackItems = attackItems;

        damageOutput = inventory.getDamage();
        cooldownOutput = inventory.getCooldown();
        projectileAmountOutput = inventory.getProjectileAmount();

        assertTrue(damageOutput == damageInput);
        assertTrue(cooldownOutput == cooldownInput);
        assertTrue(projectileAmountOutput == projectileAmountInput);
    }

    @Test
    public void testGetDefensive(){
        int healthInput = 0;
        int healthOutput;
        int healthTemp;

        float accelerationInput = 0.0f;
        float accelerationOutput;
        float accelerationTemp;

        List<SupportItem> supportItems = new ArrayList();

        for(int i = 0; i < 100; i++){
            healthTemp = random.nextInt(10);
            accelerationTemp = random.nextFloat();
            supportItems.add(new SupportItem(healthTemp, accelerationTemp));
            healthInput += healthTemp;
            accelerationInput += accelerationTemp;
        }

        inventory.supportItems = supportItems;

        healthOutput = inventory.getHealth();
        accelerationOutput = inventory.getAcceleration();

        assertTrue(healthOutput == healthInput);
        assertTrue(accelerationOutput == accelerationInput);
    }
}
