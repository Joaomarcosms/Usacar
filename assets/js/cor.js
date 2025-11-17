// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const API_URL = 'http://localhost:8080/api/cor';

// ==========================================================
// REFERÊNCIAS AOS ELEMENTOS DO DOM
// ==========================================================
const corForm = document.getElementById('corForm');
const corIdInput = document.getElementById('corId');
const nomeInput = document.getElementById('nome');
const coresTableBody = document.querySelector('#coresTable tbody');
const corCountBadge = document.getElementById('corCount');
const emptyState = document.getElementById('emptyState');
const confirmModal = document.getElementById('confirmModal');
const confirmYesBtn = document.getElementById('confirmYes');
const confirmNoBtn = document.getElementById('confirmNo');
const notification = document.getElementById('notification');

let corIdToDelete = null;

// ==========================================================
// INICIALIZAÇÃO E EVENTOS
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {
    carregarCores();
    corForm.addEventListener('submit', salvarCor);
    confirmYesBtn.addEventListener('click', () => deletarCor(corIdToDelete));
    confirmNoBtn.addEventListener('click', fecharModalConfirmacao);
});

// ==========================================================
// FUNÇÕES DE CARREGAMENTO E RENDERIZAÇÃO
// ==========================================================
async function carregarCores() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar cores.');
        const cores = await response.json();
        renderizarTabela(cores);
    } catch (error) {
        showNotification(`Erro ao carregar cores: ${error.message}`, 3000, 'error');
    }
}

function renderizarTabela(cores) {
    coresTableBody.innerHTML = '';
    emptyState.style.display = cores.length === 0 ? 'block' : 'none';

    // Ordena as cores por ID em ordem crescente
    cores.sort((a, b) => a.id - b.id);

    cores.forEach(cor => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${cor.id}</td>
            <td>${cor.nome}</td>
            <td class="actions">
                <button onclick="editarCor(${cor.id})" class="btn btn-sm btn-warning">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="abrirModalConfirmacao(${cor.id})" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        coresTableBody.appendChild(row);
    });

    corCountBadge.textContent = `${cores.length} cores`;
}

// ==========================================================
// FUNÇÕES DE CRUD (Criar, Ler, Atualizar, Deletar)
// ==========================================================
async function salvarCor(event) {
    event.preventDefault();

    const id = corIdInput.value;
    const nome = nomeInput.value;

    if (!nome.trim()) {
        showNotification('O nome da cor não pode ser vazio.', 3000, 'error');
        return;
    }

    const cor = {
        id: id ? parseInt(id) : null,
        nome: nome
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cor)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Erro ao salvar a cor.');
        }

        showNotification('Cor salva com sucesso!', 3000, 'success');
        limparFormulario();
        carregarCores();

    } catch (error) {
        showNotification(error.message, 4000, 'error');
    }
}

async function editarCor(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Cor não encontrada.');
        const cor = await response.json();

        corIdInput.value = cor.id;
        nomeInput.value = cor.nome;

        window.scrollTo({ top: 0, behavior: 'smooth' }); // Rola a página para o topo
    } catch (error) {
        showNotification(`Erro ao carregar cor para edição: ${error.message}`, 3000, 'error');
    }
}

async function deletarCor(id) {
    if (!id) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        
        if (!response.ok) {
             const errorData = await response.json().catch(() => ({}));
             throw new Error(errorData.message || 'Erro ao deletar a cor.');
        }
        
        showNotification('Cor deletada com sucesso!', 3000, 'success');
        fecharModalConfirmacao();
        carregarCores();

    } catch (error) {
        showNotification(`Erro: ${error.message}`, 4000, 'error');
        fecharModalConfirmacao();
    }
}

// ==========================================================
// FUNÇÕES AUXILIARES (Modais, Formulário, Notificações)
// ==========================================================
function limparFormulario() {
    corForm.reset();
    corIdInput.value = '';
}

function abrirModalConfirmacao(id) {
    corIdToDelete = id;
    confirmModal.classList.remove('hidden');
}

function fecharModalConfirmacao() {
    corIdToDelete = null;
    confirmModal.classList.add('hidden');
}

function showNotification(message, duration = 3000, type = 'success') {
    const notification = document.getElementById('notification');
    if (!notification) return;
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    setTimeout(() => {
        notification.className = 'notification hidden';
    }, duration);
}

