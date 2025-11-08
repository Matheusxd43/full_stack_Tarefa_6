<script setup>
import { ref, onMounted, nextTick, watchEffect } from 'vue'
import tarefaService from './services/tarefaService'

// --- ESTADO DO TEMA ---
const isDarkMode = ref(true)

const tarefas = ref([])
const novaTarefaTitulo = ref('')
const erro = ref(null)
const tarefaEditandoId = ref(null)
const tituloEditando = ref('')

// --- FUNÇÃO DE TEMA ---
function toggleTheme() {
  isDarkMode.value = !isDarkMode.value
}

// --- EFEITO DO TEMA ---
watchEffect(() => {
  if (isDarkMode.value) {
    document.body.classList.remove('light-theme')
  } else {
    document.body.classList.add('light-theme')
  }
})

// Funções de API
async function carregarTarefas() {
  try {
    erro.value = null
    const response = await tarefaService.getTarefas()
    tarefas.value = response.data.sort((a, b) => a.concluida - b.concluida || a.id - b.id)
  } catch (e) {
    erro.value = 'Falha ao carregar tarefas.'
    console.error(e)
  }
}

async function adicionarTarefa() {
  if (!novaTarefaTitulo.value.trim()) return
  const novaTarefa = {
    titulo: novaTarefaTitulo.value,
    concluida: false,
  }
  try {
    erro.value = null
    await tarefaService.addTarefa(novaTarefa)
    novaTarefaTitulo.value = ''
    await carregarTarefas()
  } catch (e) {
    erro.value = 'Falha ao adicionar tarefa.'
    console.error(e)
  }
}

async function removerTarefa(id) {
  try {
    erro.value = null
    await tarefaService.deleteTarefa(id)
    tarefas.value = tarefas.value.filter((t) => t.id !== id)
  } catch (e) {
    erro.value = 'Falha ao remover tarefa.'
    console.error(e)
  }
}

async function alternarStatus(tarefa) {
  try {
    erro.value = null
    const novoStatus = !tarefa.concluida
    const response = await tarefaService.updateStatusTarefa(tarefa.id, novoStatus)
    tarefa.concluida = response.data.concluida
    await carregarTarefas()
  } catch (e) {
    erro.value = 'Falha ao atualizar status.'
    console.error(e)
  }
}

function ativarEdicao(tarefa) {
  tarefaEditandoId.value = tarefa.id
  tituloEditando.value = tarefa.titulo
  nextTick(() => {
    document.querySelector(`#input-edit-${tarefa.id}`).focus()
  })
}

async function salvarEdicao(tarefa) {
  if (!tituloEditando.value.trim() || tituloEditando.value.trim() === tarefa.titulo) {
    cancelarEdicao()
    return
  }
  try {
    erro.value = null
    const response = await tarefaService.updateTituloTarefa(tarefa.id, tituloEditando.value)
    tarefa.titulo = response.data.titulo
    cancelarEdicao()
  } catch (e) {
    erro.value = 'Falha ao salvar edição.'
    console.error(e)
  }
}

function cancelarEdicao() {
  tarefaEditandoId.value = null
  tituloEditando.value = ''
}

onMounted(carregarTarefas)
</script>

<template>
  <button @click="toggleTheme" class="btn-theme">
    {{ isDarkMode ? '☀️' : '🌙' }}
  </button>

  <div class="container">
    <header>
      <h1>Lista de Tarefas</h1>
      <p>Aplicação Full-Stack com Vue e Spring Boot</p>
    </header>
    <main>
      <div class="form-container">
        <input
          v-model="novaTarefaTitulo"
          @keyup.enter="adicionarTarefa"
          placeholder="O que precisa ser feito?"
        />
        <button @click="adicionarTarefa">+</button>
      </div>

      <p v-if="erro" class="erro">{{ erro }}</p>

      <ul class="tarefa-lista">
        <li v-for="tarefa in tarefas" :key="tarefa.id" :class="{ concluida: tarefa.concluida }">
          <div class="info-tarefa">
            <input type="checkbox" :checked="tarefa.concluida" @change="alternarStatus(tarefa)" />
            <span v-if="tarefaEditandoId !== tarefa.id" @dblclick="ativarEdicao(tarefa)">
              {{ tarefa.titulo }}
            </span>
            <input
              v-else
              :id="`input-edit-${tarefa.id}`"
              type="text"
              v-model="tituloEditando"
              @blur="salvarEdicao(tarefa)"
              @keyup.enter="salvarEdicao(tarefa)"
              @keyup.esc="cancelarEdicao"
              class="input-edicao"
            />
          </div>
          <div class="acoes">
            <button @click="removerTarefa(tarefa.id)" class="btn-remover">×</button>
          </div>
        </li>
      </ul>
    </main>

    <footer>
      <p>
        Desenvolvido por Giovanni Zago |
        <a href="https://github.com/ZagoGiovanni" target="_blank" rel="noopener noreferrer">
          GitHub
        </a>
      </p>
    </footer>
  </div>
</template>

<style>
:root {
  --cor-fundo: #1a1a1a;
  --cor-surface: #242424;
  --cor-primaria: #646cff;
  --cor-texto: rgba(255, 255, 255, 0.87);
  --cor-borda: #424242;
  --cor-perigo: #ff6b6b;
}

body.light-theme {
  --cor-fundo: #f9f9f9;
  --cor-surface: #ffffff;
  --cor-texto: #242424;
  --cor-borda: #e0e0e0;
}

body {
  font-family: 'Inter', system-ui, Avenir, Helvetica, Arial, sans-serif;
  background-color: var(--cor-fundo);
  color: var(--cor-texto);
  margin: 0;
  display: flex;
  justify-content: center;
  padding-top: 40px;
  transition:
    background-color 0.3s,
    color 0.3s;
}

.container {
  width: 100%;
  max-width: 680px;
  margin: 0 auto;
  padding: 0 20px;
}

header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40px;
}

header h1 {
  font-size: 3.2em;
  margin: 0;
  color: var(--cor-primaria);
}

header p {
  color: #a0a0a0;
}

.btn-theme {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1000;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  padding: 0;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;

  background-color: var(--cor-surface);
  color: var(--cor-texto);
  border: 1px solid var(--cor-borda);
  cursor: pointer;
  transition: all 0.2s;
}

.btn-theme:hover {
  border-color: var(--cor-primaria);
}

.form-container {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
}

.form-container input {
  flex-grow: 1;
  padding: 12px 15px;
  font-size: 1em;
  background-color: var(--cor-surface);
  border: 1px solid var(--cor-borda);
  border-radius: 8px;
  color: var(--cor-texto);
  outline: none;
  transition:
    border-color 0.2s,
    background-color 0.3s,
    color 0.3s;
}

.form-container input:focus {
  border-color: var(--cor-primaria);
}

.form-container button {
  padding: 0;
  width: 48px;
  height: 48px;
  font-size: 2em;
  border-radius: 8px;
  border: 1px solid transparent;
  background-color: var(--cor-primaria);
  color: white;
  cursor: pointer;
  transition: background-color 0.2s;
  line-height: 48px;
}

.form-container button:hover {
  background-color: #535bf2;
}

.erro {
  color: var(--cor-perigo);
  text-align: center;
  margin-bottom: 20px;
}

.tarefa-lista {
  list-style: none;
  padding: 0;
  margin: 0;
}

.tarefa-lista li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: var(--cor-surface);
  border-bottom: 1px solid var(--cor-borda);
  transition: background-color 0.3s;
}

.tarefa-lista li:first-child {
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.tarefa-lista li:last-child {
  border-bottom: none;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.info-tarefa {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-grow: 1;
}

.info-tarefa input[type='checkbox'] {
  appearance: none;
  width: 20px;
  height: 20px;
  border: 2px solid var(--cor-borda);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.info-tarefa input[type='checkbox']:hover {
  border-color: var(--cor-primaria);
}

.info-tarefa input[type='checkbox']:checked {
  background-color: var(--cor-primaria);
  border-color: var(--cor-primaria);
}

.info-tarefa input[type='checkbox']:checked::after {
  content: '✔';
  color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 12px;
}

.info-tarefa span {
  cursor: pointer;
}

li.concluida .info-tarefa span {
  text-decoration: line-through;
  color: #a0a0a0;
}

.input-edicao {
  font-size: 1em;
  padding: 5px;
  background-color: var(--cor-fundo);
  border: 1px solid var(--cor-primaria);
  border-radius: 4px;
  color: var(--cor-texto);
  flex-grow: 1;
}

.btn-remover {
  background: none;
  border: none;
  color: #a0a0a0;
  font-size: 1.5em;
  cursor: pointer;
  transition: color 0.2s;
  visibility: hidden;
  opacity: 0;
}

li:hover .btn-remover {
  visibility: visible;
  opacity: 1;
}

.btn-remover:hover {
  color: var(--cor-perigo);
}

footer {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--cor-borda);
  color: #a0a0a0;
  font-size: 0.9em;
}

footer a {
  color: var(--cor-primaria);
  text-decoration: none;
  transition: color 0.2s;
}

footer a:hover {
  color: #8a90ff;
}
</style>
