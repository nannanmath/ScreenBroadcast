package nan.learnjava.server;

import nan.learnjava.utils.Utils;

public class TestApp {

	public static void main(String[] args) {
		long l = System.nanoTime();
		System.out.println(l + "");
		byte[] bytes = Utils.long2bytes(l);
		long l2 = Utils.bytes2long(bytes);
		System.out.println(l2 + "");
	}

}
