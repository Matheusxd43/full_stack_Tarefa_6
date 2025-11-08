package br.com.tarefas.api.service;

import br.com.tarefas.api.model.Tarefa;
import br.com.tarefas.api.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Tarefa criarTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título da tarefa é obrigatório.");
        }
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizarStatus(Long id, boolean concluida) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o id: " + id));

        tarefaExistente.setConcluida(concluida);
        return tarefaRepository.save(tarefaExistente);
    }

    public Tarefa atualizarTitulo(Long id, String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O novo título não pode ser vazio.");
        }

        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o id: " + id));

        tarefaExistente.setTitulo(titulo);
        return tarefaRepository.save(tarefaExistente);
    }

    public void deletarTarefa(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada com o id: " + id);
        }
        tarefaRepository.deleteById(id);
    }
}