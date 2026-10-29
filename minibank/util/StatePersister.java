package minibank.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import minibank.model.Account;

public class StatePersister {

    private StatePersister() {
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) ois.readObject();
        }
    }
}