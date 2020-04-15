package br.com.virtualsistemas.common.builders;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {

	protected MapBuilder(Map<K, V> source) {
		this.source = source;
	}

	public MapBuilder() {
		this(new HashMap<K, V>());
	}

	private Map<K, V> source;

	public Map<K, V> build() {
		return new HashMap<K, V>(source);
	}

	public MapBuilder<K, V> put(K key, V value) {
		source.put(key, value);
		return this;
	}

	public MapBuilder<K, V> remove(K key) {
		source.remove(key);
		return this;
	}

	public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> m) {
		source.putAll(m);
		return this;
	}

	public MapBuilder<K, V> clear() {
		source.clear();
		return this;
	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> build(K key, V value, Object... more) {
		Map<K, V> source = new HashMap<K, V>();
		source.put(key, value);
		for (int i = 0; i < more.length; i += 2) {
			source.put((K) more[i], (V) more[i + 1]);
		}
		return source;
	}
}