import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        int nextInd = size();
        if (get(r.toString()) == null) {
            storage[nextInd] = r;
        }
    }

    int getResumeIndex(String uuid) {
        int index = -1;
        int size = size();
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].toString())) {
                index = i;
                break;
            }
        }
        return index;
    }

    Resume get(String uuid) {
        Resume resume = null;
        int index = getResumeIndex(uuid);
        if (index != -1) {
            resume = storage[index];
        }
        return resume;
    }

    void delete(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            storage[index] = null;
            storage = Arrays.stream(storage).filter(Objects::nonNull).toArray(Resume[]::new);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.stream(storage).filter(Objects::nonNull).toArray(Resume[]::new);
    }

    int size() {
        return (int) Arrays.stream(storage).filter(Objects::nonNull).count();
    }
}
