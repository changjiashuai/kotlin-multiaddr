package io.ipfs.multiformats.multiaddr;

import java.io.ByteArrayOutputStream;

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/29.
 */
public class TestHex {

  public static byte[] fromHex(String hex) {
    if (hex.length() % 2 != 0) {
      throw new IllegalStateException("Uneven number of hex digits!");
    }
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    for (int i = 0; i < hex.length() - 1; i += 2)
      bout.write(Integer.valueOf(hex.substring(i, i + 2), 16));
    return bout.toByteArray();
  }
}
