import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encryption{

	// Adding filler to the password
	private static final String SALT = "I_LOVE_OOP";

	public static String encryption(String password, String SECRET_KEY, char mode){
		try {
			byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			if (mode == 'E') {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
				return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
			}
			else if (mode == 'D') {
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
				return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
			}
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e);
		}
		return null;
	}
}
