package client.proxy;

import static org.junit.Assert.*;

import org.junit.Test;
import client.data.*;

public class ServerProllerTest {

	@Test
	public void testRun() {
		MockServer server = new MockServer();
		Game model = server.UpdateMap();
		assertTrue(model != null);
	}

}
