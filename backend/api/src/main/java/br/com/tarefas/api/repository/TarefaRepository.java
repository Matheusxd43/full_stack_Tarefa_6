package br.com.tarefas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tarefas.api.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}