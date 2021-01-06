package br.com.virtualsistemas.common.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author juniorlatalisa
 */
public class ListBuilder<E> implements Builder<List<E>> {

	protected ListBuilder(List<E> source) {
		this.source = source;
	}

	public ListBuilder() {
		this(new LinkedList<>());
	}

	private List<E> source;

	@Override
	public List<E> build() {
		return new ArrayList<>(source);
	}

	public ListBuilder<E> add(E e) {
		source.add(e);
		return this;
	}

	public ListBuilder<E> remove(E e) {
		source.remove(e);
		return this;
	}

	public ListBuilder<E> addAll(Collection<? extends E> c) {
		source.addAll(c);
		return this;
	}

	public ListBuilder<E> addAll(int index, Collection<? extends E> c) {
		source.addAll(index, c);
		return this;
	}

	public ListBuilder<E> removeAll(Collection<? extends E> c) {
		source.removeAll(c);
		return this;
	}

	public ListBuilder<E> clear() {
		source.clear();
		return this;
	}

	public ListBuilder<E> set(int index, E element) {
		source.set(index, element);
		return this;
	}

	public ListBuilder<E> add(int index, E element) {
		source.add(index, element);
		return this;
	}

	public ListBuilder<E> remove(int index) {
		source.remove(index);
		return this;
	}

	public static <E> ListBuilder<E> builder() {
		return new ListBuilder<E>();
	}

	@SafeVarargs
	public static <E> ListBuilder<E> builder(E element, E... more) {
		return new ListBuilder<E>(build(element, more));
	}

	public static <E> ListBuilder<E> builder(Collection<E> elements) {
		return new ListBuilder<E>(new ArrayList<E>(elements));
	}

	@SafeVarargs
	public static <E> List<E> build(E element, E... more) {
		List<E> source = new ArrayList<E>(more.length + 1);
		source.add(element);
		for (E e : more) {
			source.add(e);
		}
		return source;
	}
}