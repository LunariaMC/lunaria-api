package net.lunaria.api.core.redis.bucket;

import com.google.gson.reflect.TypeToken;
import net.lunaria.api.core.redis.RedisManager;

import java.lang.reflect.Type;
import java.util.*;

public class RedisList<E> extends RedisBucket<List<E>> implements List<E> {
    public RedisList(RedisManager redisManager, String key, Type type) {
        super(redisManager, key, type);
    }

    @Override
    protected List<E> setupObject() {
        List<E> content = new ArrayList<>();
        List<String> list = gson.fromJson(original, new TypeToken<List<String>>(){}.getType());

        if (list != null) {
            for (String jsonElement: list) {
                E element = gson.fromJson(jsonElement, this.type);
                if(element != null) content.add(element);
            }
        }
        return content;
    }

    @Override
    protected String serialize() {
        List<String> list = new ArrayList<>();

        for (E element : this) list.add(gson.toJson(element));

        return gson.toJson(list);
    }

    @Override
    public int size() {
        return this.result.size();
    }

    @Override
    public boolean isEmpty() {
        return this.result.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.result.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.result.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.result.toArray();
    }

    @Override
    public <T> T[] toArray( T[] a) {
        return this.result.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return this.result.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.result.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.result.contains(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.result.addAll(c);
    }

    @Override
    public boolean addAll(int index,Collection<? extends E> c) {
        return this.result.addAll(index,c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.result.removeAll(c);
    }

    @Override
    public boolean retainAll( Collection<?> c) {
        return this.result.retainAll(c);
    }

    @Override
    public void clear() {
        this.result.clear();
    }

    @Override
    public E get(int index) {
        return this.result.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.result.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        this.result.add(index, element);
    }

    @Override
    public E remove(int index) {
        return this.result.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.result.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.result.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.result.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.result.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.result.subList(fromIndex, toIndex);
    }
}
