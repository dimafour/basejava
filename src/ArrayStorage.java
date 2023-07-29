/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < size(); i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        storage[size()] = r;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].uuid.equals(uuid)) return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int j = i; j < size(); j++) {
                    storage[j] = storage[j + 1];
                }
            }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] storageTrimmed = new Resume[size()];
        System.arraycopy(storage, 0, storageTrimmed, 0, size());
        return storageTrimmed;
    }

    int size() {
        int i;
        for (i = 0; i < storage.length; i++) {
            if (storage[i] == null) return i;
        }
        return i;
    }
}
