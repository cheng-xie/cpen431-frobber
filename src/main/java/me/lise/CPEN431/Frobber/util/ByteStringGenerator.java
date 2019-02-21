package me.lise.CPEN431.Frobber.util;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import com.google.protobuf.ByteString;

public class ByteStringGenerator {
	private AtomicLong count;
	public ByteStringGenerator() {
		reset();
	}
	
	public ByteString getNextByteString(int length) {
		long cur_count = this.count.addAndGet(1);
		assert(cur_count<Long.MAX_VALUE);
		assert(length>=Long.BYTES);
		return ByteString.copyFrom(ByteBuffer.allocate(length).putLong(cur_count).array()); 
	}

	public void reset() {
		this.count.set(0);
	}
}
