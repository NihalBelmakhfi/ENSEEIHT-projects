package allumettes;

import org.junit.*;
import static org.junit.Assert.assertEquals;

public class RapideTest {

	private strategie strategie;

	@Before
	public void setUp() {
		this.strategie = new Rapide();
	}

	@Test
	public void testergetPrise() throws CoupInvalideException {
		assertEquals(this.strategie.getPrise(new JeuReel(13)), 3);
		assertEquals(this.strategie.getPrise(new JeuReel(3)), 3);
		assertEquals(this.strategie.getPrise(new JeuReel(2)), 2);
		assertEquals(this.strategie.getPrise(new JeuReel(10)), 3);
		assertEquals(this.strategie.getPrise(new JeuReel(1)), 1);

	}

	@Test
	public void testertoString() {
		assertEquals(this.strategie.toString(), "rapide");
	}
}
