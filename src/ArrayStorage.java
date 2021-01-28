import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    int initialSize = 10_000;
    Resume[] storage = new Resume[initialSize];
    int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        if (get(r.toString()) == null) {
            storage[size++] = r;
        }
    }

    int getResumeIndex(String uuid) {
        int index = -1;
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
            int length = size - (index + 1);
            if (length >= 0) {
                System.arraycopy(storage, index + 1, storage, index, length);
            }
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.stream(storage, 0, size).toArray(Resume[]::new);
    }

    int size() {
        return size;
    }
}
