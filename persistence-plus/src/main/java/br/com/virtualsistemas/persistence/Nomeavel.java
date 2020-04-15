package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.PersistenceException;

public interface Nomeavel extends Serializable {

	final Comparator<Nomeavel> COMPARATOR_POR_NOME = (cn1, cn2) -> cn1.getNome().compareTo(cn2.getNome());

	String getNome();

	default void setNome(String nome) {
		throw new PersistenceException(nome);
	}
}