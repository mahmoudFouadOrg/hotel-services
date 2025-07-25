package org.mfouad.entities;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UuidGenerator implements IdentifierGenerator{
	
	public static UUID generate() {
		
        long timestamp = Instant.now().toEpochMilli();
        byte[] timestampBytes = ByteBuffer.allocate(8).putLong(timestamp).array();

        byte[] randomBytes = new byte[10];
        ThreadLocalRandom.current().nextBytes(randomBytes);

        byte[] uuidBytes = new byte[16];
        System.arraycopy(timestampBytes, 2, uuidBytes, 0, 6); // Take last 6 bytes of timestamp
        System.arraycopy(randomBytes, 0, uuidBytes, 6, 10);

        uuidBytes[6] &= 0x0f; // Clear version bits
        uuidBytes[6] |= 0x70; // Set version to 7

        uuidBytes[8] &= 0x3f; // Clear variant bits
        uuidBytes[8] |= 0x80; // Set variant to RFC 4122

        ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();

        return new UUID(mostSigBits, leastSigBits);
    }

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		  long timestamp = Instant.now().toEpochMilli();
	        byte[] timestampBytes = ByteBuffer.allocate(8).putLong(timestamp).array();

	        byte[] randomBytes = new byte[10];
	        ThreadLocalRandom.current().nextBytes(randomBytes);

	        byte[] uuidBytes = new byte[16];
	        System.arraycopy(timestampBytes, 2, uuidBytes, 0, 6); // Take last 6 bytes of timestamp
	        System.arraycopy(randomBytes, 0, uuidBytes, 6, 10);

	        uuidBytes[6] &= 0x0f; // Clear version bits
	        uuidBytes[6] |= 0x70; // Set version to 7

	        uuidBytes[8] &= 0x3f; // Clear variant bits
	        uuidBytes[8] |= 0x80; // Set variant to RFC 4122

	        ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
	        long mostSigBits = buffer.getLong();
	        long leastSigBits = buffer.getLong();

	        return new UUID(mostSigBits, leastSigBits);
	}

}
