package util;

import java.io.*;

/**
 * A tool used to serialize or unserialize a object.
 */
public class SerializeUtil {
    public static byte[] serialize(Object object) {

        ObjectOutputStream oos = null;

        ByteArrayOutputStream baos = null;

        try {

            baos = new ByteArrayOutputStream();

            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);

            byte[] bytes = baos.toByteArray();

            return bytes;

        } catch (Exception e) {

        }

        return null;

    }

    public static Object unSerialize(byte[] bytes) {

        ByteArrayInputStream bais = null;

        try {

            //反序列化

            bais = new ByteArrayInputStream(bytes);

            ObjectInputStream ois = new ObjectInputStream(bais);

            return ois.readObject();

        } catch (Exception e) {

        }

        return null;

    }
}
