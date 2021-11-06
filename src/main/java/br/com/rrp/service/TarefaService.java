package br.com.rrp.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rrp.java.Data;
import br.com.rrp.model.Tarefa;
import br.com.rrp.repository.TarefasRepository;
import br.com.rrp.resources.exceptions.ParametroInvalidoException;
import br.com.rrp.resources.exceptions.RecursoNaoEncontradoException;

@Service
public class TarefaService {

	@Autowired
	private TarefasRepository tarefasRepository;

	public List<Tarefa> listar() {

		return tarefasRepository.findAll();
	}

	public Optional<Tarefa> buscarId(Long id) {

		if (id == null) {
			throw new ParametroInvalidoException("ID não informado.");
		}

		Optional<Tarefa> tarefa = tarefasRepository.findById(id);

		return tarefa;
	}

	public List<Tarefa> buscarCumpridas() {

		List<Tarefa> tarefas = tarefasRepository.findAll();

		List<Tarefa> tarefasCumpridas = new ArrayList<Tarefa>();

		for (Iterator<Tarefa> tarefasIt = tarefas.iterator(); tarefasIt.hasNext();) {
			Tarefa tarefa = tarefasIt.next();

			if (tarefa.isCumprida()) {
				tarefasCumpridas.add(tarefa);
			}

		}

		return tarefasCumpridas;
	}

	public List<Tarefa> buscarNaoCumpridas() {

		List<Tarefa> tarefas = tarefasRepository.findAll();

		List<Tarefa> tarefasNaoCumpridas = new ArrayList<Tarefa>();

		for (Iterator<Tarefa> tarefasIt = tarefas.iterator(); tarefasIt.hasNext();) {
			Tarefa tarefa = tarefasIt.next();

			if (!tarefa.isCumprida()) {
				tarefasNaoCumpridas.add(tarefa);
			}

		}

		return tarefasNaoCumpridas;
	}

	public Tarefa salvar(Tarefa tarefa) {
		
		if (tarefa.getDescricao() == null) {
			throw new ParametroInvalidoException("A descrição deve ser informada.");
		}
		
		
		if (tarefa.getNome() == null) {
			throw new ParametroInvalidoException("O nome da terefa deve ser informado.");
		}
		
		if (tarefa.getDataMaxima() == null) {
			throw new ParametroInvalidoException("A data terefa deve ser informada.");
		}
		
		if(!Data.isValida(tarefa.getDataMaxima(), "dd/MM/yyyy")) {
			throw new ParametroInvalidoException("A data terefa deve ser informada com formato dd/MM/yyyy.");
		}

		return tarefasRepository.save(tarefa);
	}
	
	public Optional<Tarefa> marcarCumprida(Long id) {

		if (id == null) {
			throw new ParametroInvalidoException("ID não informado.");
		}
		
		Optional<Tarefa> tarefa = tarefasRepository.findById(id);

		if (tarefa.isPresent()) {

			if (!tarefa.get().isCumprida()) {
				tarefa.get().setCumprida(true);
			} else {
				tarefa.get().setCumprida(false);
			}

			tarefasRepository.save(tarefa.get());
		}else {
			throw new RecursoNaoEncontradoException("ID informado não encontrado.");
		}

		return tarefa;
	}
}
