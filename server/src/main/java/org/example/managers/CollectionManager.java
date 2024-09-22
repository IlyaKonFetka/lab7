package org.example.managers;

import org.example.Main;
import org.example.database.CollectionTableHelper;
import org.example.exceptions.ValidateException;
import org.example.model.Person;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class CollectionManager {
    private ConcurrentSkipListSet<Person> collection;
    private LocalDateTime lastInitTime;
    private CollectionTableHelper collectionTableHelper;

    public CollectionManager(CollectionTableHelper collectionTableHelper) {
        this.lastInitTime = null;
        this.collection = new ConcurrentSkipListSet<>(Comparator.comparing(Person::getName));
        this.collectionTableHelper = collectionTableHelper;
    }

    public LocalDateTime getLastInitTime() throws NullPointerException {
        return lastInitTime;
    }

    public void setLastInitTime() {
        this.lastInitTime = LocalDateTime.now();
    }

    public ConcurrentSkipListSet<Person> getCollection() {
        return collection;
    }

    public Person byId(int id) {
        return collection.stream()
                .filter(person -> person.getID() == id)
                .findFirst()
                .orElse(null);
    }
    public List<Person> getSortedById() {
        List<Person> sortedList = new ArrayList<>(collection);
        sortedList.sort(Comparator.comparing(Person::getId));
        return sortedList;
    }
    public boolean isContain(Person e) {
        return e != null && collection.stream().anyMatch(person -> person.getID() == e.getID());
    }

    public boolean update(Person updatedPerson, long userID) {
        Person existingPerson = byId(updatedPerson.getID());
        if (existingPerson != null && existingPerson.getOwnerID() == userID) {
            collection.remove(existingPerson);
            updatedPerson.setOwnerID(userID);
            updatedPerson.setCreationDate(existingPerson.getCreationDate());
            if (collectionTableHelper.update(updatedPerson, userID)) {
                collection.add(updatedPerson);
                return true;
            }
        }
        return false;
    }
    public boolean append(Person person, long userId) {
        try {
            person.setOwnerID(userId);

            Integer assignedId = collectionTableHelper.insertAndGetId(person, userId);

            if (assignedId != null) {
                person.setId(assignedId);

                return collection.add(person);
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean remove(Person a, long userId) {
        try {
            if (collectionTableHelper.delete(a.getID(), userId)) {
                return collection.remove(a);
            }
            return false;
        } catch (SQLException e) {
            Main.logger.error("Error removing person from database: " + e.getMessage());
            return false;
        }
    }

    public boolean remove(int id, long userId) {
        try {
            if (collectionTableHelper.delete(id, userId)) {
                return collection.removeIf(person -> person.getID() == id);
            }
            return false;
        } catch (SQLException e) {
            Main.logger.error("Error removing person from database: " + e.getMessage());
            return false;
        }
    }

    public boolean cleanCollection(long userId) {
        try {
            if (collectionTableHelper.deleteAll(userId)) {
                collection.clear();
                return true;
            }
            return false;
        } catch (SQLException e) {
            Main.logger.error("Error cleaning collection in database: " + e.getMessage());
            return false;
        }
    }

    public String getMaxName() {
        return collection.stream()
                .map(Person::getName)
                .max(String::compareTo)
                .orElse("");
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        return collection.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n\n"))
                .trim();
    }

    public int cleanCollectionForUser(long userId) {
        int removedCount = 0;
        try {
            List<Integer> idsToRemove = collectionTableHelper.deleteAllForUser(userId);
            for (Integer id : idsToRemove) {
                if (collection.removeIf(person -> person.getID() == id && person.getOwnerID() == userId)) {
                    removedCount++;
                }
            }
            return removedCount;
        } catch (SQLException e) {
            Main.logger.error("Error cleaning collection for user in database: " + e.getMessage());
            return 0;
        }
    }

    public boolean loadCollection() throws ValidateException {
        collection = new ConcurrentSkipListSet<>();
        TreeSet<Person> loadedPersons = collectionTableHelper.readCollection();

        for (Person person : loadedPersons) {
            if (!person.validate()) {
                throw new ValidateException("Validation failed for person with ID: " + person.getId());
            }
            collection.add(person);
        }
        return true;
    }
}
