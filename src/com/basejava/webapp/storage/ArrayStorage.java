import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_SIZE = 10_000;
    private Resume[] storage = new Resume[STORAGE_SIZE];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (get(r.toString()) == null) {
            if (size == STORAGE_SIZE) {
                System.out.printf("Невозможно добавить резюме с uuid %s: хранилище заполнено.", r.toString());
            } else {
                storage[size++] = r;
            }
        } else {
            System.out.printf("Резюме с uuid %s уже существует.", r.toString());
        }
    }

    private int getResumeIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].toString())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public Resume get(String uuid) {
        Resume resume = null;
        int index = getResumeIndex(uuid);
        if (index != -1) {
            resume = storage[index];
        } else {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.", uuid);
        }
        return resume;
    }

    public void delete(String uuid) {
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

    public void update(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            storage[index].setUuid(uuid);
        } else {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.", uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.stream(storage, 0, size).toArray(Resume[]::new);
    }

    public int size() {
        return size;
    }
}
