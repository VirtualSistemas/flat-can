package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Logger;

import javax.validation.Validation;
import javax.validation.constraints.Size;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.virtualsistemas.persistence.jpa.JPAFacade;

public abstract class TesteEntidade<E extends Entidade> {

	protected static final Logger log = Logger.getLogger("TesteEntidade");

	@BeforeClass
	static void beforeClass() {
		log.info("Validador configurado: " + Validation.buildDefaultValidatorFactory().getValidator());
		/*
		<dependency>
			<groupId>org.glassfish</groupId>
			<artifactId>javax.el</artifactId>
			<version>3.0.1-b08</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>6.0.2.Final</version>
			<scope>test</scope>
		</dependency>
		*/
	}

	protected abstract E criar();

	protected abstract JPAFacade getFacade();

	protected void testeCopiavel(E origem, Copiavel<E> destino) {
		if (!remover(origem)) {
			throw new RuntimeException("Não removeu");
		}
		destino.copiar(origem);
		getFacade().insert(destino);
	}

	protected void nomeVazio(Nomeavel entidade) {
		entidade.setNome("");
		getFacade().update(entidade);
	}
	
	protected void nomeNulo(Nomeavel entidade) {
		entidade.setNome(null);
		getFacade().update(entidade);
	}

	protected void nomeSize(Nomeavel entidade) {
		Size size = null;
		{
			Class<?> _class = entidade.getClass();
			while (!_class.equals(Object.class)) {
				try {
					size = _class.getDeclaredField("nome").getAnnotation(Size.class);
					break;
				} catch (NoSuchFieldException | SecurityException e) {
					_class = _class.getSuperclass();
				}
			}
			if (size == null) {
				throw new RuntimeException("nomeSize fail: " + entidade);
			}
		}
		if (size.min() > 0) {
			entidade.setNome("");
		} else if (size.max() > 0) {
			entidade.setNome(new String(new char[1 + size.max()]));
		} else {
			entidade.setNome(null);
		}
		getFacade().update(entidade);
	}

	protected E alterar(E entidade) {
		((Nomeavel) entidade).setNome(UUID.randomUUID().toString());
		return getFacade().update(entidade);
	}

	protected Serializable getPrimaryKey(E entidade) {
		if (entidade instanceof Codificavel<?>) {
			return ((Codificavel<?>) entidade).getCodigo();
		}
		if (entidade instanceof EntidadeComposta) {
			return ((EntidadeComposta) entidade).getId();
		}
		if (entidade instanceof Identificavel<?>) {
			return ((Identificavel<?>) entidade).getIdentificador();
		}
		throw new IllegalAccessError("Unknow entity: " + entidade);
	}

	@SuppressWarnings("unchecked")
	protected E pesquisar(E entidade) {
		return (E) getFacade().find(entidade.getClass(), getPrimaryKey(entidade));
	}

	protected boolean remover(E entidade) {
		return getFacade().delete(entidade.getClass(), getPrimaryKey(entidade));
	}

	@Test
	public void basico() {
		E entidade1 = criar();

		E entidade2;

		if (!entidade1.equals(entidade2 = pesquisar(entidade1))) {
			Assert.fail("Entidades não são iguais [insert]");
			return;
		}

		if (entidade1.equals(alterar(entidade2))) {
			Assert.fail("Entidades são iguais");
			return;
		}

		if (!entidade2.equals(entidade2 = pesquisar(entidade1))) {
			Assert.fail("Entidades não são iguais [update]");
			return;
		}

		if (!((remover(entidade1)) && (pesquisar(entidade1) == null))) {
			Assert.fail("Objeto não foi removido");
		}
	}
}