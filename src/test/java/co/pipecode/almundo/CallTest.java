package co.pipecode.almundo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import co.pipecode.almundo.domain.Call;

public class CallTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCallCreationWithInvalidParameter() {
		new Call(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRandomCallCreationWithInvalidFirstParameter() {
		new Call(-1, 1, 1).buildRandomCall();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRandomCallCreationWithInvalidSecondParameter() {
		new Call(1, -1, 1).buildRandomCall();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRandomCallCreationWithInvalidThirdParameter() {
		new Call(1, 1, -1).buildRandomCall();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRandomCallCreationWithInvalidMinMaxParameterOrder() {
		new Call(1, 2, 1).buildRandomCall();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRandomCallCreationWithInvalidParameters() {
		int defaultDuration = -1;
		int minDuration = -1;
		int maxDuration = -1;
		Call call = new Call(defaultDuration, minDuration, maxDuration).buildRandomCall();

		assertNotNull(call);
		assertTrue(minDuration <= call.getDuration());
		assertTrue(call.getDuration() <= maxDuration);
	}

	@Test
	public void testRandomCallCreationWithValidParameters() {
		int defaultDuration = 1;
		int minDuration = 2;
		int maxDuration = 8;
		Call call = new Call(defaultDuration, minDuration, maxDuration).buildRandomCall();

		assertNotNull(call);
	}

	@Test
	public void testRandomCallsCreationWithValidParameters() {
		int totalCalls = 10;
		int defaultDuration = 1;
		int minDuration = 2;
		int maxDuration = 8;
		List<Call> calls = new Call(defaultDuration, minDuration, maxDuration).buildRandomCalls(totalCalls);

		assertNotNull(calls);
		assertEquals(totalCalls, calls.size());
	}
}
